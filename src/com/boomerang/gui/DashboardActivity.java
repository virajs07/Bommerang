package com.boomerang.gui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

import com.boomerang.customlayouts.BoomerangToast;

/**
 * 
 * @author Anurag Rao
 * @version 1.0
 * @since June, 2011
 * 
 */

public class DashboardActivity extends Activity implements OnClickListener {

	private ImageButton imageButton;
	private ImageButton statusButton;
	private ImageButton fileButton;
	private ImageButton reviewButton;
	private Intent transmittingIntent;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dashboard);

		initViews();
		setListeners();
	}

	/**
	 * Initializes all the buttons defined above
	 */
	private void initViews() {
		imageButton = (ImageButton) findViewById(R.id.imagebutton);
		statusButton = (ImageButton) findViewById(R.id.statusbutton);
		fileButton = (ImageButton) findViewById(R.id.filebutton);
		reviewButton = (ImageButton) findViewById(R.id.reviewbutton);
	}

	/**
	 * Sets the listeners for the buttons defined above
	 */
	private void setListeners() {
		imageButton.setOnClickListener(this);
		statusButton.setOnClickListener(this);
		fileButton.setOnClickListener(this);
		reviewButton.setOnClickListener(this);
	}

	/**
	 * The separate on-click listeners for each of the buttons
	 * 
	 * @param v
	 *            The view which was selected by the user
	 */
	public void onClick(View v) {
		if (v == imageButton) {
			transmittingIntent = new Intent().setClass(this,
					ImageSwipeScreenActivity.class);
			startActivity(transmittingIntent);
		} else if (v == statusButton) {
			(new BoomerangToast(this, "This feature is not available yet"))
					.show();
			/*
			 * transmittingIntent = new Intent().setClass(this,
			 * SwipeScreenActivity.class); transmittingIntent.putExtra("Menu",
			 * R.menu.menu_status); transmittingIntent.putExtra("Type",
			 * "Status"); startActivity(transmittingIntent);
			 */
		} else if (v == fileButton) {
			// TODO Put intent code here
			(new BoomerangToast(this, "This feature is not available yet"))
					.show();
		} else if (v == reviewButton) {
			transmittingIntent = new Intent().setClass(this,
					ReviewActivity.class);
			startActivity(transmittingIntent);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_dashboard, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case R.id.menu_item_dashboard_settings:
			// TODO Code here for preferences
			(new BoomerangToast(this, "This feature is not available yet"))
					.show();
			return true;
		default:
			return false;
		}
	}
}