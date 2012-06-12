package com.boomerang.customlayouts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.boomerang.gui.R;

/**
 * 
 * @author Anurag Rao
 * @version 1.0
 * @since June, 2011
 *
 */
public class BoomerangToast extends LinearLayout {
	private Toast toast;
	private TextView toastText;

	public BoomerangToast(Context context, String text) {
		super(context);
		toast = new Toast(context);
		toast.setDuration(Toast.LENGTH_SHORT);

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.boomerangtoast, this, true);

		toast.setView(view);

		toastText = (TextView) findViewById(R.id.toast);
		toastText.setText(text);
	}

	public void show() {
		toast.show();
	}
}
