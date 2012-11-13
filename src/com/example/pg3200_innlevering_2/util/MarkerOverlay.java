package com.example.pg3200_innlevering_2.util;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import com.example.pg3200_innlevering_2.dto.FlickrImageDTO;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class MarkerOverlay extends ItemizedOverlay<OverlayItem> {

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
		// Description = date taken. This makes a LOT of sense. Trust me.
		OverlayItem overlayItem = new OverlayItem(geoPoint, image.getTitle(), image.getDateTaken());
		markers.add(overlayItem);
		
		// Update the map
		populate();
	}
	
	protected boolean onTap(int arg0) {
		return super.onTap(arg0);
	}
	
	public void draw(Canvas arg0, MapView arg1, boolean arg2) {
		super.draw(arg0, arg1, false);
	}
}
