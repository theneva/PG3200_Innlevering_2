package com.example.pg3200_innlevering_2.ui;

import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.TabContentFactory;

import com.example.pg3200_innlevering_2.R;
import com.example.pg3200_innlevering_2.dto.FlickrImageDTO;
import com.example.pg3200_innlevering_2.ui.adapters.ImageAdapter;
import com.example.pg3200_innlevering_2.util.ImageUtil;
import com.example.pg3200_innlevering_2.util.MarkerOverlay;
import com.example.pg3200_innlevering_2.util.TagUtil;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

// Can't use AbstractActivity because it has a map. Oh Google?
public class SearchResultsActivity extends MapActivity {

	private Context context;
	
	private TabHost tabHost;
	
	// List stuff
	private ListView listViewImages;
	private ImageAdapter adapter;
	
	
	// Map stuff
	private MapView mapView;
	private MapController mapController;
	private MarkerOverlay markerOverlay;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_results);
		
		context = this;
		
		initGui();
	}
	
	protected void initGui() {
		// Update the label; kind of hackish, but I don't see any prettier way
		this.setTitle(this.getTitle() + " \"" + TagUtil.getLastUsedTag() + "\"");
		
		// Not extending TabActivity and not inflating the tab host from XML
		tabHost = (TabHost) findViewById(android.R.id.tabhost);
		tabHost.setup();
		
		// Set up the list view
		listViewImages = (ListView) findViewById(R.id.listViewImages);

		// Set up the map view; markers are added when images are retrieved
		mapView = (MapView) findViewById(R.id.mapView);
		mapView.setBuiltInZoomControls(true);
		mapController = mapView.getController();
		
		Drawable drawable = getResources().getDrawable(R.drawable.marker);
		markerOverlay = new MarkerOverlay(drawable);
		
		// Add the tabs
		tabHost.addTab(tabHost.newTabSpec("List").setIndicator("List").setContent(new TabContentFactory() {
			public View createTabContent(String arg0) {
				return listViewImages;
			}
		}));
		
		tabHost.addTab(tabHost.newTabSpec("Map").setIndicator("Map").setContent(new TabContentFactory() {
			public View createTabContent(String arg0) {
				return mapView;
			}
		}));
		
		// Hackish: Prevent the map from "bleeding through"
		tabHost.setCurrentTabByTag("Map");
		tabHost.setCurrentTabByTag("List");
		
		// Get results and initialise adapter
		new GetFlickrImageListTask().execute();
	}
	
	/**
	 * Returns to the tag manager activity.
	 */
	public void onBackPressed() {
		Intent intent = new Intent(this, TagManagerActivity.class);
		// Only necessary is program is expanded by more activities
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}
	
	private class GetFlickrImageListTask extends AsyncTask<Void, Void, List<FlickrImageDTO>> {
		
		private ProgressDialog progressDialog;
		
		protected void onPreExecute() {
			// These should be downloaded individually by the adapter, but I couldn't find a good solution. Sorry!
			progressDialog = ProgressDialog.show(context, "Downloading images in a horribly inefficient manner", "Please wait while I finish rendering the AsyncTask useless...");
		}
		
		protected List<FlickrImageDTO> doInBackground(Void... params) {
			// Possible TODO: publish progress
			return ImageUtil.getImages();
		}
		
		protected void onPostExecute(List<FlickrImageDTO> result) {
			progressDialog.dismiss();
			
			// TODO: Give feedback instead of returning nothing
			if (result == null) return;
			
			// List stuff
			adapter = new ImageAdapter(context, result);
			listViewImages.setAdapter(adapter);
			
			// Map stuff
			for (FlickrImageDTO image : result) {
				// TODO: This isn't very semantically correct or expandable, is it...
				markerOverlay.addItem(image);
			}
			
			mapView.getOverlays().add(markerOverlay);
			
			// Seemingly pointless positioning + hard coding = best thing ever
			mapController.setCenter(new GeoPoint(59910191, 10766602));
			mapController.setZoom(5);
		}
	}
	
	// Oh, Google...
	protected boolean isRouteDisplayed() {
		return false;
	}
}
