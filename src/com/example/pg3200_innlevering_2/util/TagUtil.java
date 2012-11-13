package com.example.pg3200_innlevering_2.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TagUtil {
	private static List<String> tags = new ArrayList<String>();
	private static String lastUsedTag;

	// TODO: Rename?
	public static boolean addTag(String tag) {
		if (tag == null || tag.equals("") || tag.trim().equals("")) return false;
		
		// Make sure the tag doesn't already exist
		tag = tag.toLowerCase();
		return tags.contains(lastUsedTag = tag) ? false : tags.add(tag);
	}

	/**
	 * Make sure the most recent tags are displayed first.
	 * @return the list of tags, reversed for convenience.
	 */
	public static List<String> getTags() {
		List<String> tagsReversed = tags;
		Collections.reverse(tagsReversed);
		
		// Make sure the last used tag is always on top
		if (tagsReversed.size() > 0 && !tagsReversed.get(0).equals(lastUsedTag)) {
			tagsReversed.remove(lastUsedTag);
			tagsReversed.add(0, lastUsedTag);
		}
		
		return tagsReversed;
	}

	public static String getLastUsedTag() {
		return lastUsedTag;
	}
}
