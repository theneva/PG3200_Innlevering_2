package com.example.pg3200_innlevering_2.ui;

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
import com.example.pg3200_innlevering_2.ui.adapters.TagAdapter;
import com.example.pg3200_innlevering_2.util.AbstractActivity;
import com.example.pg3200_innlevering_2.util.TagUtil;

public class TagManagerActivity extends AbstractActivity {

	private EditText editTextSearch;
	private Button buttonSearch;
	
	private ListView listViewTags;
	private ListAdapter adapter;
	
	protected void init() {
		super.init(R.layout.activity_tag_manager);
	}

	protected void initGui() {
		editTextSearch = (EditText) findViewById(R.id.editTextSearch);
		buttonSearch = (Button) findViewById(R.id.buttonSearch);
		
		listViewTags = (ListView) findViewById(R.id.listViewTags);
		adapter = new TagAdapter(context, TagUtil.getTags());
		listViewTags.setAdapter(adapter);
	}

	protected void initListeners() {
		buttonSearch.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				TagUtil.addTag(editTextSearch.getText().toString());
				startActivity(new Intent(context, SearchResultsActivity.class));
			}
		});
		
		listViewTags.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				TagUtil.addTag((String) adapter.getItem(position));
				startActivity(new Intent(context, SearchResultsActivity.class));
			}
		});
	}
}
