package com.example.pg3200_innlevering_2.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.TabContentFactory;
import android.widget.TextView;

import com.example.pg3200_innlevering_2.R;
import com.example.pg3200_innlevering_2.dto.FlickrImageDTO;
import com.example.pg3200_innlevering_2.ui.adapters.ImageAdapter;
import com.example.pg3200_innlevering_2.util.ImageUtil;
import com.example.pg3200_innlevering_2.util.TagUtil;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

// Can't use AbstractActivity because it has a map. Oh Google?
public class SearchResultsActivity extends MapActivity {

	private Context context;
	
	private TabHost tabHost;
	
	// List stuff
	private ListView listViewImages;
	private List<FlickrImageDTO> images;
	private ImageAdapter adapter;
	
	// Map stuff
	private MapView mapView;
	private MapController mapController;
	private MarkerOverlay markerOverlay;
	
	// Full screen view stuff
	private TextView textViewFullScreenImageTitle, textViewFullScreenImageDateTaken;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_results);
		
		context = this;
		
		initGui();
		initListeners();
	}
	
	protected void initGui() {
		// Update the label; kind of hackish, but it does the job
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
//		tabHost.setCurrentTabByTag("Map");
//		tabHost.setCurrentTabByTag("List");
		
		// Get results and initialise adapter
		new GetFlickrImageListTask().execute();
	}
	
	protected void initListeners() {
		listViewImages.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int location, long arg3) {
				displayImageFullScreen(location);
			}
		});
	}
	
	protected void displayImageFullScreen(int location) {
		Dialog dialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
		
		dialog.setContentView(R.layout.full_screen_image);
		
		// Set up the components
		ImageView imageViewFullScreenImage = (ImageView) dialog.findViewById(R.id.imageViewFullScreenImage);
		textViewFullScreenImageTitle = (TextView) dialog.findViewById(R.id.textViewFullScreenImageTitle);
		textViewFullScreenImageDateTaken = (TextView) dialog.findViewById(R.id.textViewFullScreenImageDateTaken);
		
		FlickrImageDTO image = images.get(location);
		imageViewFullScreenImage.setImageDrawable(image.getDrawableM());
		textViewFullScreenImageTitle.setText(image.getTitle());
		textViewFullScreenImageDateTaken.setText(image.getDateTaken());
		
		// Can be dismissed by pressing Back
		dialog.setCancelable(true);
		dialog.show();
	}
	
	/**
	 * Returns to the tag manager activity.
	 */
	public void onBackPressed() {
		
		// Get rid of the markers
		markerOverlay.clear();
		
		Intent intent = new Intent(this, TagManagerActivity.class);
		// Only necessary is program is expanded by more activities
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}
	
	private class GetFlickrImageListTask extends AsyncTask<Void, Void, List<FlickrImageDTO>> {
		
		private ProgressDialog progressDialog;
		
		protected void onPreExecute() {
			// These should be downloaded individually by the adapter, but I couldn't find a good solution, and this does fulfill the assignment's requirements. Sorry!
			progressDialog = ProgressDialog.show(context, getResources().getText(R.string.progress_dialog_title), getResources().getText(R.string.progress_dialog_message));
		}
		
		protected List<FlickrImageDTO> doInBackground(Void... params) {
			// TODO: Add a timeout
			// Possible TODO: publish progress
			return ImageUtil.getImages();
		}
		
		protected void onPostExecute(List<FlickrImageDTO> result) {
			progressDialog.dismiss();
			
			// TODO: Give feedback instead of returning nothing?
			if (result == null) return;
			
			images = result;
			
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
	
	// Requires access to the dialog
	private class MarkerOverlay extends ItemizedOverlay<OverlayItem> {

		private List<OverlayItem> markers;
		
		public MarkerOverlay(Drawable marker) {
			super(boundCenterBottom(marker));
			markers = new ArrayList<OverlayItem>();
		}

		protected OverlayItem createItem(int position) {
			return markers.get(position);
		}

		public int size() {
			return markers.size();
		}

		public void addItem(FlickrImageDTO image) {
			
			GeoPoint geoPoint = new GeoPoint(image.getLatitude(), image.getLongitude());
			// Description = date taken. This makes a LOT of sense. Trust me. ...sorry.
			OverlayItem overlayItem = new OverlayItem(geoPoint, image.getTitle(), image.getDateTaken());
			markers.add(overlayItem);
			
			// Update the map
			populate();
		}
		
		public void clear() {
			markers.clear();
		}
		
		protected boolean onTap(int location) {
			displayImageFullScreen(location);
			return super.onTap(location);
		}
	}
}
