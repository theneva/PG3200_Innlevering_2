package com.example.pg3200_innlevering_2.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class QueryDAO {
	private static final String TABLE = "queries";
	
	private static final String AUTO_ID_COLUMN = "id";
	private static final String QUERY_COLUMN = "query";
	
	private SQLiteDatabase db;
	private DBHelper dbHelper;
	
	private static final String CREATE_TABLE = "CREATE TABLE " + TABLE + " ( " + AUTO_ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT, " + QUERY_COLUMN + " TEXT NULL );";
	
	public QueryDAO(Context context) {
		if (dbHelper == null) {
			dbHelper = new DBHelper(context);
		}
	}
	
	public QueryDAO open(boolean readOnly) {
		if (db == null || !db.isOpen()) {
			if (readOnly) {
				db = dbHelper.getReadableDatabase();
			} else {
				db = dbHelper.getWritableDatabase();
			}
		}
		
		return this;
	}
	
	public void close() {
		if (db != null && db.isOpen()) {
			db.close();
		}
	}
	
	public List<String> getAllQueries() {
		List<String> queries = new ArrayList<String>();
		open(true);
		
		Cursor cursor = db.query(TABLE, new String[] { QUERY_COLUMN }, null, null, null, null, null, null);
		
		if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
			do {
				queries.add(cursor.getString(cursor.getColumnIndex(QUERY_COLUMN)));
			} while (cursor.moveToNext());
		}
		
		close();
		
		return queries;
	}
	
	public boolean insertQuery(String query) {

		ContentValues values = new ContentValues(1);
		values.put(QUERY_COLUMN, query);
		open(false);
		
		boolean success = db.insert(TABLE, QUERY_COLUMN, values) > 0;
		close();
		
		return success;
	}
	
	// No need to implement update or delete in this program. Could be used for e.g. keeping track
	// of search word "hits" and the like
	
	private static class DBHelper extends SQLiteOpenHelper {
		private static final String DATABASE_NAME = "flickr_image_search_db";
		
		// Create database
		public DBHelper(Context context) {
			super(context, DATABASE_NAME, null, 1);
		}
		
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(CREATE_TABLE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		}
	}
}
