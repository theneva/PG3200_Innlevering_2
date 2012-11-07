package com.example.pg3200_innlevering_2.ui.adapters;

import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pg3200_innlevering_2.R;
import com.example.pg3200_innlevering_2.dto.FlickrImageDTO;
import com.example.pg3200_innlevering_2.util.FlickrUtil;

public class ImageAdapter extends BaseAdapter{
	
	private List<FlickrImageDTO> images;
	private LayoutInflater inflater;
	
	public ImageAdapter(Context context, List<FlickrImageDTO> images) {
		this.images = images;
		this.inflater = LayoutInflater.from(context);
	}

	public int getCount() {
		if (images != null) return images.size();
		else return 0;
	}

	public Object getItem(int position) {
		return images.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.list_item_image, null);
			
			viewHolder = new ViewHolder();
			viewHolder.imageViewImage = (ImageView) convertView.findViewById(R.id.imageViewImage);
			viewHolder.textViewImageTitle = (TextView) convertView.findViewById(R.id.textViewImageTitle);
			viewHolder.textViewImageDateTaken = (TextView) convertView.findViewById(R.id.textViewImageDateTaken);
			
			FlickrImageDTO image = images.get(position);
			
			Drawable drawable = FlickrUtil.getImageFromUrl(image.getUrlSq());
			viewHolder.imageViewImage.setImageDrawable(drawable);
			
			viewHolder.textViewImageTitle.setText(image.getTitle());
			viewHolder.textViewImageDateTaken.setText(image.getDateTaken());
			
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		return convertView;
	}
	
	private class ViewHolder {
		private ImageView imageViewImage;
		private TextView textViewImageTitle, textViewImageDateTaken;
	}
}
