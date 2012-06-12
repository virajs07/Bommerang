package com.boomerang.gui;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;

import com.boomerang.customlayouts.BoomerangToast;

/**
 * 
 * @author Anurag Rao
 * @version 1.0
 * @since June, 2011
 */
public class ReviewActivity extends Activity implements OnClickListener {

	// elements if the GUI
	private RatingBar ratingStars;
	private EditText reviewText;
	private EditText firstNameText;
	private EditText lastNameText;
	private EditText emailIDText;
	private ImageButton submitButton;
	private CheckBox phoneInfoCheckbox;

	// the information taken from the user during review
	private float rating;
	private String review;
	private String firstName;
	private String lastName;
	private String emailID;
	private PhoneInfo phoneInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.review);

		init();
		submitButton.setOnClickListener(this);
	}

	private void init() {
		ratingStars = (RatingBar) findViewById(R.id.reviewRating);
		reviewText = (EditText) findViewById(R.id.reviewText);
		firstNameText = (EditText) findViewById(R.id.firstNameText);
		lastNameText = (EditText) findViewById(R.id.lastNameText);
		emailIDText = (EditText) findViewById(R.id.emailIDText);
		submitButton = (ImageButton) findViewById(R.id.submitButton);
		phoneInfoCheckbox = (CheckBox) findViewById(R.id.phoneInfo);
	}

	public void onClick(View v) {
		boolean isValidEmailID = true;
		rating = ratingStars.getRating();
		review = reviewText.getText().toString();
		emailID = emailIDText.getText().toString();
		isValidEmailID = isValidEmailID(emailID);
		firstName = firstNameText.getText().toString();
		lastName = lastNameText.getText().toString();

		if (phoneInfoCheckbox.isChecked()) {// takes phone info only when user
											// allows it
			phoneInfo = new PhoneInfo();
		}

		// check if details are valid and the necessary ones have been entered
		// properly
		if (review.equals("") || emailID.equals("") || !isValidEmailID) {
			AlertDialog alertDialog = new AlertDialog.Builder(this).create();
			alertDialog.setTitle("Error");
			alertDialog.setMessage(review.equals("") ? "Please enter a review."
					: (emailID.equals("") ? "Please enter your email ID."
							: "Please enter a valid email ID."));
			alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
				}
			});
			alertDialog.show();
		} else {
			(new BoomerangToast(this, "Thank you.")).show();
			// TODO run thread to upload info to website and email us the phone
			// details
			finish();
		}
	}

	private boolean isValidEmailID(String email) {
		Pattern p = Pattern
				.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))"
						+ "@((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]"
						+ "?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?[0-9]{1,2}"
						+ "|25[0-5]|2[0-4][0-9])\\.([0-1]?[0-9]{1,2}|25[0-5]|2[0-4]"
						+ "[0-9])){1}|([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$");
		Matcher m = p.matcher(email);
		return m.matches();
	}

	private class PhoneInfo {

		public final String brand;
		public final String device;
		public final String display;
		public final String fingerprint;
		public final String host;
		public final String manufacturer;
		public final String model;
		public final String product;
		public final String type;
		public final String user;
		public int sdk;

		PhoneInfo() {
			brand = android.os.Build.BRAND;
			device = android.os.Build.DEVICE;
			display = android.os.Build.DISPLAY;
			fingerprint = android.os.Build.FINGERPRINT;
			host = android.os.Build.HOST;
			manufacturer = android.os.Build.MANUFACTURER;
			model = android.os.Build.MODEL;
			product = android.os.Build.PRODUCT;
			type = android.os.Build.TYPE;
			user = android.os.Build.USER;
			sdk = android.os.Build.VERSION.SDK_INT;
		}
	}
}
