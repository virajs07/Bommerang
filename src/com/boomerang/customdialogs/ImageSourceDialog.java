package com.boomerang.customdialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

import com.boomerang.gui.ImageSwipeScreenActivity;
import com.boomerang.gui.R;

/**
 * 
 * @author Anurag Rao
 * @version 1.0
 * @since June, 2011
 * 
 */
public class ImageSourceDialog extends Dialog implements OnClickListener {

	/**
	 * The context from which it was called. It's only going to be
	 * ImageSwipeScreenActivity.
	 */
	private ImageSwipeScreenActivity context;
	private ImageButton dialogCameraButton;
	private ImageButton dialogGalleryButton;

	public ImageSourceDialog(Context context, int theme) {
		super(context, theme);
		// getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
		// WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
		this.context = (ImageSwipeScreenActivity) context;
		setContentView(R.layout.dialogimage);
		setCancelable(true);
		this.

		init();
		setListeners();
	}

	private void init() {
		dialogCameraButton = (ImageButton) findViewById(R.id.dialogcamerabutton);
		dialogGalleryButton = (ImageButton) findViewById(R.id.dialoggallerybutton);
	}

	private void setListeners() {
		dialogCameraButton.setOnClickListener(this);
		dialogGalleryButton.setOnClickListener(this);
	}

	public void onClick(View v) {
		if (v == dialogCameraButton) {
			dismiss();
			context.transmitCameraIntent();
		} else if (v == dialogGalleryButton) {
			dismiss();
			context.transmitGalleryIntent(null);
		}
	}

	/**
	 * Returns back to the dashboard if the back button is pressed.
	 */
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		if (!context.calledDialogFromMenu)
			context.onBackPressed();
	}
}
