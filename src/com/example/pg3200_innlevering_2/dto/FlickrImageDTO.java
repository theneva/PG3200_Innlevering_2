package com.example.pg3200_innlevering_2.dto;

import android.graphics.drawable.Drawable;


public final class FlickrImageDTO {
	private final String title;
	private final String urlSq, urlM;
	private final Drawable drawableSq, drawableM;
	private final int latitude, longitude;
	private final String dateTaken;

	// Accepts latitude and longitude as floats (ie. ab.cdefgh)
	public FlickrImageDTO(String title, String urlSq, String urlM, Drawable drawableSq, Drawable drawableM, double latitude, double longitude, String dateTaken) {
		this.title = title;
		this.urlSq = urlSq;
		this.urlM = urlM;
		this.drawableSq = drawableSq;
		this.drawableM = drawableM;
		
		// Get eight digits for use with GeoPoint
		this.latitude = (int) (latitude * 1E6);
		this.longitude = (int) (longitude * 1E6);
		
		this.dateTaken = dateTaken;
	}

	public String getTitle() {
		return title;
	}

	public String getUrlSq() {
		return urlSq;
	}

	public String getUrlM() {
		return urlM;
	}
	
	public Drawable getDrawableSq() {
		return drawableSq;
	}
	
	public Drawable getDrawableM() {
		return drawableM;
	}

	public int getLatitude() {
		return latitude;
	}

	public int getLongitude() {
		return longitude;
	}

	public String getDateTaken() {
		return dateTaken;
	}
}
