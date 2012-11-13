package com.example.pg3200_innlevering_2.ui;

import java.util.Collections;
import java.util.List;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.pg3200_innlevering_2.R;
import com.example.pg3200_innlevering_2.db.QueryDAO;
import com.example.pg3200_innlevering_2.ui.adapters.TagAdapter;
import com.example.pg3200_innlevering_2.util.AbstractActivity;

public class TagManagerActivity extends AbstractActivity {

	private EditText editTextSearch;
	private Button buttonSearch;
	
	private QueryDAO queryDAO;
	
	private ListView listViewTags;
	private ListAdapter adapter;
	
	protected void init() {
		super.init(R.layout.activity_tag_manager);
	}

	protected void initGui() {
		editTextSearch = (EditText) findViewById(R.id.editTextSearch);
		buttonSearch = (Button) findViewById(R.id.buttonSearch);
		
		queryDAO = new QueryDAO(this);
		
		listViewTags = (ListView) findViewById(R.id.listViewTags);
		
		List<String> queriesReversed = queryDAO.getAllQueries();
		Collections.reverse(queriesReversed);
		
		adapter = new TagAdapter(context, queriesReversed);
		listViewTags.setAdapter(adapter);
	}

	protected void initListeners() {
		buttonSearch.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				
				String query = editTextSearch.getText().toString();
				
				// make sure there's an actual query
				if (query == null || query.equals("")) return;
				
				// Make sure there are no dupe entries (case sensitive; TODO?)
				if (!queryDAO.getAllQueries().contains(query)) {
					queryDAO.insertQuery(query);
				}
				
				Intent intent = new Intent(new Intent(context, SearchResultsActivity.class));
				startActivity(intent.putExtra("query", query));
			}
		});
		
		listViewTags.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				Intent intent = new Intent(context, SearchResultsActivity.class);
				intent.putExtra("query", queryDAO.getAllQueries().get(position));
				startActivity(new Intent(context, SearchResultsActivity.class));
			}
		});
	}
}
