package com.example.pg3200_innlevering_2.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public abstract class AbstractActivity extends Activity {

	protected Context context;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		
		init();
		initGui();
		initListeners();
	}
	
	protected void init(int layout) {
		setContentView(layout);
	}
	
	protected abstract void init();
	protected abstract void initGui();
	protected abstract void initListeners();
	
	protected void navigateToActivity(Class<?> activity) {
		super.startActivity(new Intent(context, activity));
	}
}
