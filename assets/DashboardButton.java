package com.boomerang.customlayouts;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.boomerang.gui.R;

/**
 * 
 * @author Anurag Rao
 * @version 1.0
 * @since June, 2011
 * 
 */
public class DashboardButton extends LinearLayout {

	private Drawable drawable;
	public String text;
	public int textSize;

	/**
	 * TextView that contains the name of the section
	 */
	public BoomerangTextView title;
	/**
	 * ImageButton that contains the drawable to display
	 */
	public ImageButton button;

	public DashboardButton(Context context) {
		super(context);

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.buttondashboard, this, true);

		initView();
	}

	public DashboardButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.buttondashboard, this, true);

		TypedArray array = context.obtainStyledAttributes(attrs,
				R.styleable.DashboardButton);

		text = array.getString(R.styleable.DashboardButton_text);
		textSize = array.getDimensionPixelOffset(
				R.styleable.DashboardButton_textSize, 16);
		drawable = array.getDrawable(R.styleable.DashboardButton_drawable);

		array.recycle();

		initView();
	}

	private void initView() {
		button = (ImageButton) findViewById(R.id.button);
		title = (BoomerangTextView) findViewById(R.id.title);

		button.setImageDrawable(drawable);
		title.setTextSize(textSize);
		title.setText(text);
	}
}
