package com.boomerang.gui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.boomerang.customlayouts.BoomerangToast;

/**
 * 
 * @author Anurag Rao
 * @version 1.0
 * @since June,2011
 * 
 */

public class BoomerangGalleryActivity extends Activity implements
		OnClickListener {

	private int count;
	private int countSelected;

	// variables to contain all the details of the images and ones selected
	private Bitmap[] thumbnails;
	private boolean[] isSelected;
	private String[] imagePath;
	private int[] imageID;
	private int[] selectedImageID;

	private ImageAdapter imageAdapter;
	private Cursor imageCursor;
	private ImageButton okayButton;

	private Intent receivingIntent;
	private Intent transmittingIntent;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.boomeranggallery);

		startCursor();
		init();
		imageCursor.close();

		okayButton = (ImageButton) findViewById(R.id.okayButton);
		okayButton.setOnClickListener(this);
	}

	private void startCursor() {
		// get all the id's of the images in storage
		final String[] columns = { MediaColumns.DATA, BaseColumns._ID };
		final String orderBy = BaseColumns._ID + " DESC";
		// execute SQL query to obtain all the images
		imageCursor = managedQuery(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null,
				null, orderBy);
		// TODO line below has to be changed
		startManagingCursor(imageCursor);
	}

	private void init() {
		count = imageCursor.getCount();
		countSelected = 0;
		thumbnails = new Bitmap[count];
		imagePath = new String[count];
		imageID = new int[count];
		isSelected = new boolean[count];
		receivingIntent = getIntent(); // initialize with the intent which
										// called this activity
		transmittingIntent = new Intent();

		selectedImageID = receivingIntent.getExtras()
				.getIntArray("Selected_ID");
		if (selectedImageID != null)
			countSelected = selectedImageID.length;

		populateGrid();

		GridView imagegrid = (GridView) findViewById(R.id.imageGrid);
		imageAdapter = new ImageAdapter();
		imagegrid.setAdapter(imageAdapter);
	}

	private void populateGrid() {
		int idColumnIndex = imageCursor.getColumnIndex(BaseColumns._ID), dataColumnIndex = imageCursor
				.getColumnIndex(MediaColumns.DATA);

		// initialize the grid with the thumbnails of the images in storage
		for (int i = 0; i < this.count; i++) {
			imageCursor.moveToPosition(i);
			// initialize image paths
			imagePath[i] = imageCursor.getString(dataColumnIndex);
			// initialize image Ids
			imageID[i] = imageCursor.getInt(idColumnIndex);
			// initialize thumbnails
			thumbnails[i] = MediaStore.Images.Thumbnails.getThumbnail(
					getContentResolver(), imageID[i],
					MediaStore.Images.Thumbnails.MICRO_KIND, null);
			// if the image has already been selected mark it
			if (selectedImageID != null && exists(imageID[i]))
				isSelected[i] = true;
		}
	}

	private boolean exists(int id) {
		int lp = 0, up = selectedImageID.length - 1, mp;
		while (lp <= up) {
			mp = (lp + up) / 2;
			if (selectedImageID[mp] == id)
				return true;
			if (selectedImageID[mp] < id)
				lp = mp + 1;
			else
				up = mp - 1;
		}
		return false;
	}

	public void onClick(View v) {
		if (countSelected == 0) {// make sure atleast one image is selected
			(new BoomerangToast(this, "Please select at least one image."))
					.show();
		} else {
			String selectedImagePath[] = new String[countSelected];
			selectedImageID = new int[countSelected];
			int j = 0;

			for (int i = 0; i < isSelected.length; i++)
				if (isSelected[i]) {
					selectedImagePath[j] = imagePath[i];
					selectedImageID[j++] = imageID[i];
				}

			// put them in the intent to send back to the caller
			transmittingIntent.putExtra("Selected_Path", selectedImagePath);
			transmittingIntent.putExtra("Selected_ID", selectedImageID);

			setResult(RESULT_OK, transmittingIntent);
			finish();
		}
	}

	public class ImageAdapter extends BaseAdapter {
		private LayoutInflater mInflater;

		public ImageAdapter() {
			mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		public int getCount() {
			return count;
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			final ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.boomeranggalleryitem,
						null);
				holder.thumbContainer = (RelativeLayout) convertView
						.findViewById(R.id.thumbContainer);
				holder.imageview = (ImageView) convertView
						.findViewById(R.id.thumbImage);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.thumbContainer.setId(position);
			holder.imageview.setId(position);

			holder.imageview.setImageBitmap(thumbnails[position]);
			if (isSelected[position])
				holder.imageview
						.setBackgroundResource(R.drawable.image_frame_thick_selected);

			holder.imageview.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					int id = v.getId();
					if (isSelected[id]) {
						isSelected[id] = false;
						holder.imageview
								.setBackgroundResource(R.drawable.image_frame_thick_normal);
						countSelected--;
					} else {
						isSelected[id] = true;
						holder.imageview
								.setBackgroundResource(R.drawable.image_frame_thick_selected);
						countSelected++;
					}
				}
			});

			holder.thumbContainer.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					int id = v.getId();
					if (isSelected[id]) {
						isSelected[id] = false;
						holder.imageview
								.setBackgroundResource(R.drawable.image_frame_thick_normal);
						countSelected--;
					} else {
						isSelected[id] = true;
						holder.imageview
								.setBackgroundResource(R.drawable.image_frame_thick_selected);
						countSelected++;
					}
				}
			});
			return convertView;
		}
	}

	class ViewHolder {
		RelativeLayout thumbContainer;
		ImageView imageview;
	}
}