package com.example.pg3200_innlevering_2.util;

/**
 * Workaround because I can't get Intent's put- and get-<?>Extra methods to work properly.
 * The public String should be a deadly sin, but at least it works.
 */
public class QueryUtil {
	public static String LAST_QUERY_USED = null;
}
