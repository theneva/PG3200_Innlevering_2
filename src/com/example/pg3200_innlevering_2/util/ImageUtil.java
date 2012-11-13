package com.example.pg3200_innlevering_2.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.drawable.Drawable;

import com.example.pg3200_innlevering_2.dto.FlickrImageDTO;

public class ImageUtil {
	
	private static final String URL = "http://api.flickr.com/services/rest/?method=flickr.photos.search&nojsoncallback=1&format=json&api_key=6b3b39e81d8f4b5f527250506e146d4b&sort=date-posteddesc&has_geo=true&extras=url_sq,url_m,geo,date_taken&per_page=10&tags=%22";
	
	/**
	 * Gets the images from the stream with the given tag.
	 * @return the images with the given tag.
	 */
	public static List<FlickrImageDTO> getImages() {
		URL url = null;
		HttpURLConnection urlConnection = null;
		
		try {
			// Accept the last used tag
			url = new URL(URL + TagUtil.getLastUsedTag());
			urlConnection = (HttpURLConnection) url.openConnection();
			
			InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
			
			return parseFlickrJson(readStream(inputStream));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch(IOException e) {
			e.printStackTrace();
			System.out.println("Unable to establish connection with Flickr.");
		} finally {
			if (urlConnection != null) {
				urlConnection.disconnect();
			}
		}
		
		return null;
	}
	
	/**
	 * Manually parsing the JSON.
	 * @param json the JSON input stream.
	 * @return a list of FlickrImageDtos retrieved from the stream.
	 */
	private static List<FlickrImageDTO> parseFlickrJson(String json) {
		List<FlickrImageDTO> images = new ArrayList<FlickrImageDTO>();
		
		// unnamed : Object containing (photos : Object containing photo : array containing unnamed : Object) AND stat : String
		try {
			JSONObject jsonTotal = new JSONObject(json);
			JSONObject imageTotal = jsonTotal.optJSONObject("photos");
			JSONArray imageSet = imageTotal.optJSONArray("photo");
			
			for (int i = 0; i < imageSet.length(); i++) {
				JSONObject image = imageSet.optJSONObject(i);
				
				String title = image.optString("title");
				String urlSq = image.optString("url_sq");
				String urlM = image.optString("url_m");
				Drawable drawableSq = ImageUtil.getImageFromUrl(urlSq);
				Drawable drawableM = ImageUtil.getImageFromUrl(urlM);
				double latitude = image.optDouble("latitude");
				double longitude = image.optDouble("longitude");
				String dateTaken = image.optString("datetaken");
				
				images.add(new FlickrImageDTO(title, urlSq, urlM, drawableSq, drawableM, latitude, longitude, dateTaken));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return images;
	}
	
	/**
	 * Reads the input stream.
	 * @param inputStream the input stream to read.
	 * @return the stream converted to one String (in this case: JSON).
	 */
	private static String readStream(InputStream inputStream) {
		String line;
		
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
			StringBuilder total = new StringBuilder();
			
			while ((line = in.readLine()) != null)
				total.append(line);
			
			return total.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	/**
	 * Gets an image from the given url as a Drawable.
	 * @param url the location of the image.
	 * @return the retrieved Drawable.
	 */
	public static Drawable getImageFromUrl(String url) {
		InputStream in = null;
		
		try {
			in = new BufferedInputStream(new URL(url).openStream());
			Drawable drawable = Drawable.createFromStream(in, "src");
			System.out.println("Drawable has been created, x: " + drawable.getIntrinsicWidth() + ", y: " + drawable.getIntrinsicHeight());
			return drawable;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return null;
	}
}
