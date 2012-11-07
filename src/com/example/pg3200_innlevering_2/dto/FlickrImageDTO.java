package com.example.pg3200_innlevering_2.dto;


public final class FlickrImageDTO {
	private final String title;
	private final String urlSq, urlM;
	private final int latitude, longitude;
	private final String dateTaken;
	
	public FlickrImageDTO(String title, String urlSq, String urlM, int latitude, int longitude, String dateTaken) {
		this.title = title;
		this.urlSq = urlSq;
		this.urlM = urlM;
		this.latitude = latitude;
		this.longitude = longitude;
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
