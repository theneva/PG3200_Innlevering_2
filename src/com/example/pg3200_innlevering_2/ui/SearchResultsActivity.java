package com.example.pg3200_innlevering_2.ui;

import java.util.List;

import android.content.Intent;
import android.os.AsyncTask;
import android.widget.ListView;

import com.example.pg3200_innlevering_2.R;
import com.example.pg3200_innlevering_2.dto.FlickrImageDTO;
import com.example.pg3200_innlevering_2.ui.adapters.ImageAdapter;
import com.example.pg3200_innlevering_2.util.AbstractActivity;
import com.example.pg3200_innlevering_2.util.FlickrUtil;
import com.example.pg3200_innlevering_2.util.TagUtil;

public class SearchResultsActivity extends AbstractActivity {

	private ListView listViewImages;
	private ImageAdapter adapter;
	
	protected void init() {
		super.init(R.layout.activity_search_results);
	}

	protected void initGui() {
		// Update the label; kind of hackish
		this.setTitle(this.getTitle() + " \"" + TagUtil.getLastUsedTag() + "\"");
		
		listViewImages = (ListView) findViewById(R.id.listViewImages);
				
		// Get results and initialise adapter
		new GetImageTask().execute();
	}

	protected void initListeners() {

	}
	
	/**
	 * Returns to the tag manager activity.
	 */
	public void onBackPressed() {
		Intent intent = new Intent(this, TagManagerActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}
	
	private final class GetImageTask extends AsyncTask<Void, Void, List<FlickrImageDTO>> {
		protected void onPreExecute() {
			super.onPreExecute();
		}
		
		protected List<FlickrImageDTO> doInBackground(Void... params) {
			return FlickrUtil.getImages();
		}
		
		protected void onPostExecute(List<FlickrImageDTO> images) {
			// Display each image along with title and date taken
			adapter = new ImageAdapter(context, images);
			listViewImages.setAdapter(adapter);
			
			super.onPostExecute(images);
		}
	}
}
