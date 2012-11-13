package com.example.pg3200_innlevering_2.ui.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pg3200_innlevering_2.R;
import com.example.pg3200_innlevering_2.dto.FlickrImageDTO;

public class ImageAdapter extends BaseAdapter{
	
	private List<FlickrImageDTO> images;
	private LayoutInflater inflater;
	
	public ImageAdapter(Context context, List<FlickrImageDTO> images) {
		this.images = images;
		this.inflater = LayoutInflater.from(context);
	}

	public int getCount() {
		// TODO: Give feedback if no results were found
		// Prevent program from crashing if no images were retrieved
		return images != null ? images.size() : 0;
	}

	public Object getItem(int position) {
		return images.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder = null;
		
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.list_item_image, null);
			
			viewHolder = new ViewHolder();
			viewHolder.imageViewImage = (ImageView) convertView.findViewById(R.id.imageViewImage);
			viewHolder.textViewImageTitle = (TextView) convertView.findViewById(R.id.textViewImageTitle);
			viewHolder.textViewImageDateTaken = (TextView) convertView.findViewById(R.id.textViewImageDateTaken);
			
			FlickrImageDTO image = images.get(position);
			
			viewHolder.imageViewImage.setImageDrawable(image.getDrawableSq());
			
			viewHolder.textViewImageTitle.setText(image.getTitle());
			viewHolder.textViewImageDateTaken.setText(image.getDateTaken());
			
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		return convertView;
	}
	
	private class ViewHolder {
		ImageView imageViewImage;
		TextView textViewImageTitle, textViewImageDateTaken;
	}
}
