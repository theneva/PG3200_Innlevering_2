package com.example.pg3200_innlevering_2.ui.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.pg3200_innlevering_2.R;

public final class TagAdapter extends BaseAdapter {
	private List<String> tags;
	private LayoutInflater inflater;
	
	private final int colorLightGrey, colorWhite;
	
	public TagAdapter(Context context, List<String> tags) {
		this.tags = tags;
		
		colorLightGrey = context.getResources().getColor(R.color.light_grey);
		colorWhite = context.getResources().getColor(R.color.white);
		
		this.inflater = LayoutInflater.from(context);
	}

	public int getCount() {
		return tags.size();
	}

	public Object getItem(int position) {
		return tags.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.list_item_tag, null);
			
			viewHolder = new ViewHolder();
			viewHolder.textViewTag = (TextView) convertView.findViewById(R.id.textViewTag);
			viewHolder.linearLayoutListTagContainer = (View) convertView.findViewById(R.id.linearLayoutTagContainer);
			
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		// Set background color based on position (grey-white-grey-white...).
		viewHolder.linearLayoutListTagContainer.setBackgroundColor(position % 2 == 0 ? colorLightGrey : colorWhite);
		viewHolder.textViewTag.setText(tags.get(position));
		
		return convertView;
	}
	
	private static class ViewHolder {
		private TextView textViewTag;
		private View linearLayoutListTagContainer;
	}
}
