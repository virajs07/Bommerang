package com.boomerang.gui;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.SlidingDrawer;
import android.widget.ViewSwitcher;

import com.boomerang.customdialogs.ImageSourceDialog;
import com.boomerang.customlayouts.BoomerangToast;
import com.boomerang.otherstuff.PlayView;

/**
 * 
 * @author Anurag Rao
 * @version 1.0
 * @since June,2011
 * 
 */
public class ImageSwipeScreenActivity extends Activity implements
		OnClickListener, AdapterView.OnItemSelectedListener,
		ViewSwitcher.ViewFactory {

	// elements in the screen
	private ImageButton addCaption;
	private ImageButton deleteImage;
	private SlidingDrawer thumbDrawer;
	private Gallery thumbGallery;
	public ImageSwitcher thumbSwitcher;

	// for sliding the image
	private PlayView playView;
	private FrameLayout graphicsHolder;
	private ImageAdapter imageAdapter;

	private Intent transmittingIntent;
	private ImageSourceDialog imageSourceDialog;

	// data from gallery activity is stored here
	private String imagePaths[];
	private int selectedImageIDs[];
	private int displayedImageIndex;

	// temporary path for the image from camera
	private String cameraImageFilePath = Environment
			.getExternalStorageDirectory().getAbsolutePath() + "/tmp_image.jpg";

	// TODO Make a note on the different constants used for Intents
	private static final int CAMERA_REQUEST_CODE = 1;
	private static final int GALLERY_REQUEST_CODE = 2;
	private static final float scaleFactor = 0.85f;

	private static int screenX;
	private static int screenY;

	public boolean calledDialogFromMenu = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.swipe);

		init();
		setListeners();
		// TODO when no images in gallery there is a bug
		try {
			imageSourceDialog.show();
		} catch (NullPointerException e) {
			(new BoomerangToast(this, "No images in gallery.")).show();
		}
	}

	private void init() {
		screenX = ((WindowManager) getSystemService(Context.WINDOW_SERVICE))
				.getDefaultDisplay().getWidth();
		screenY = ((WindowManager) getSystemService(Context.WINDOW_SERVICE))
				.getDefaultDisplay().getHeight();
		correctXY();

		addCaption = (ImageButton) findViewById(R.id.addCaption);
		deleteImage = (ImageButton) findViewById(R.id.deleteImage);
		thumbDrawer = (SlidingDrawer) findViewById(R.id.thumbDrawer);
		thumbGallery = (Gallery) findViewById(R.id.thumbGallery);
		thumbSwitcher = (ImageSwitcher) findViewById(R.id.thumbSwitcher);

		thumbSwitcher.setFactory(this);
		thumbSwitcher.setInAnimation(AnimationUtils.loadAnimation(this,
				R.anim.fade));
		thumbSwitcher.setOutAnimation(AnimationUtils.loadAnimation(this,
				R.anim.fade));

		graphicsHolder = (FrameLayout) findViewById(R.id.swipeScreen);
		setPlayView();

		selectedImageIDs = null;
		displayedImageIndex = 0;

		imageSourceDialog = new ImageSourceDialog(this,
				android.R.style.Theme_Translucent_NoTitleBar);
		imageSourceDialog.setCanceledOnTouchOutside(false);
	}

	private void correctXY() {
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);

		switch (metrics.densityDpi) {
		case DisplayMetrics.DENSITY_HIGH:
			screenY -= 48;
			break;
		case DisplayMetrics.DENSITY_MEDIUM:
			screenY -= 32;
			break;
		case DisplayMetrics.DENSITY_LOW:
			screenY -= 24;
			break;
		default:
			break;
		}
	}

	/**
	 * Initializes the PlayView object which is used in manipulating the
	 * position of the object to animate it's movement.
	 */
	private void setPlayView() {
		playView = new PlayView(this, thumbSwitcher);
		graphicsHolder.addView(playView);
	}

	private void setListeners() {
		addCaption.setOnClickListener(this);
		deleteImage.setOnClickListener(this);

		thumbGallery.setOnItemSelectedListener(this);
	}

	// TODO finish this
	public void onClick(View v) {
		if (v == addCaption) {
			(new BoomerangToast(this, "This feature is not available yet"))
					.show();
		} else if (v == deleteImage) {
			if (imagePaths.length == 1) {// only one image in the gallery
				thumbDrawer.animateClose();
				thumbSwitcher.setImageDrawable(null);
				imagePaths = null;
				System.gc();// calling the garbage collector explicitly
				imageSourceDialog.show();
			} else {
				String tempPaths[] = new String[imagePaths.length - 1];
				int tempIDs[] = new int[selectedImageIDs.length - 1], j = 0;
				for (int i = 0; i < imagePaths.length; i++) {
					if (i != displayedImageIndex) {
						tempPaths[j] = imagePaths[i];
						tempIDs[j++] = selectedImageIDs[i];
					}
				}

				imagePaths = tempPaths;
				selectedImageIDs = tempIDs;
				displayedImageIndex -= (displayedImageIndex == imagePaths.length) ? 1
						: 0;

				imageAdapter = new ImageAdapter(this);
				thumbGallery.setAdapter(imageAdapter);
				thumbGallery.setSelection(displayedImageIndex, true);
			}
		}
	}

	public void onItemSelected(AdapterView<?> parent, View v, int position,
			long id) {
		Bitmap temp = BitmapFactory.decodeFile(imagePaths[position]);
		int width, height;
		if (isLandscape(temp)) {
			width = screenX;
			height = (int) (temp.getHeight() * ((float) width / temp.getWidth()));
		} else {
			height = (int) (scaleFactor * screenY);
			width = (int) (temp.getWidth() * ((float) height / temp.getHeight()));
		}

		thumbSwitcher.setImageDrawable(new BitmapDrawable(
				android.media.ThumbnailUtils.extractThumbnail(temp, width,
						height,
						android.media.ThumbnailUtils.OPTIONS_RECYCLE_INPUT)));
		displayedImageIndex = position;
	}

	public void onNothingSelected(AdapterView<?> parent) {
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_image, menu);
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		if (selectedImageIDs != null)
			menu.getItem(1).setEnabled(true);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_item_image_add:
			calledDialogFromMenu = true;
			imageSourceDialog.show();
			return true;
		case R.id.menu_item_image_change:
			transmitGalleryIntent(selectedImageIDs);
			return true;
		case R.id.menu_item_image_edges:
			// TODO Put code for network change
			(new BoomerangToast(this, "This feature is not available yet"))
					.show();
			return true;
		default:
			return false;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {

		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case CAMERA_REQUEST_CODE:
				imagePaths = new String[1];
				imagePaths[0] = cameraImageFilePath;
				displayedImageIndex = 0;

				break;
			case GALLERY_REQUEST_CODE:
				imagePaths = intent.getExtras().getStringArray("Selected_Path");
				selectedImageIDs = intent.getExtras()
						.getIntArray("Selected_ID");
				displayedImageIndex = 0;

				imageAdapter = new ImageAdapter(this);
				thumbGallery.setAdapter(imageAdapter);

				break;
			}
		} else if (resultCode == RESULT_CANCELED && imagePaths == null) {
			imageSourceDialog.show();
		}
	}

	/**
	 * Checks whether the given input is landscape or portrait
	 * @param image The bitmap which is to be checked
	 * @return true if landscape false otherwise
	 */
	private boolean isLandscape(Bitmap image) {
		return ((float) image.getWidth() / image.getHeight()) > 1 ? true
				: false;
	}

	public View makeView() {
		ImageView i = new ImageView(this);
		i.setScaleType(ImageView.ScaleType.FIT_CENTER);
		i.setLayoutParams(new ImageSwitcher.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		return i;
	}

	public void transmitCameraIntent() {
		transmittingIntent = new Intent(
				android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		transmittingIntent.putExtra(MediaStore.EXTRA_OUTPUT,
				Uri.fromFile(new File(cameraImageFilePath)));
		startActivityForResult(transmittingIntent, CAMERA_REQUEST_CODE);
	}

	public void transmitGalleryIntent(int[] ids) {
		transmittingIntent = new Intent().setClass(this,
				BoomerangGalleryActivity.class);
		transmittingIntent.putExtra("Selected_ID", ids);
		startActivityForResult(transmittingIntent, GALLERY_REQUEST_CODE);
	}

	public class ImageAdapter extends BaseAdapter {
		private Context context;

		public ImageAdapter(Context c) {
			context = c;
		}

		public int getCount() {
			return selectedImageIDs.length;
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView i = new ImageView(context);

			i.setImageBitmap(MediaStore.Images.Thumbnails.getThumbnail(
					getContentResolver(), selectedImageIDs[position],
					MediaStore.Images.Thumbnails.MICRO_KIND, null));
			i.setBackgroundResource(R.drawable.image_frame_thin_normal);
			i.setAdjustViewBounds(true);
			i.setLayoutParams(new Gallery.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			return i;
		}
	}
}
