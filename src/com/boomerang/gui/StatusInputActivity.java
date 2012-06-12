package com.boomerang.gui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class StatusInputActivity extends Activity {

	private String status;
	private Intent receivingIntent;
	private Intent transmittingIntent;

	private static final int CALL_AGAIN = 3;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		init();

		StatusDialog dialog = new StatusDialog(this);
		dialog.setCanceledOnTouchOutside(false);

		dialog.show();
	}

	private void init() {
		transmittingIntent = new Intent();
		receivingIntent = getIntent();
		status = receivingIntent.getExtras().getString("Status");
	}

	public class StatusDialog extends Dialog implements OnClickListener {

		private StatusInputActivity context;
		private EditText statusText;
		private Button okayButton;
		private Button cancelButton;

		public StatusDialog(Context context) {
			super(context);
			this.context = (StatusInputActivity) context;
			setContentView(R.layout.dialogstatus);
			setCancelable(true);

			initView();
		}

		private void initView() {
			statusText = (EditText) findViewById(R.id.edit_text_status);
			okayButton = (Button) findViewById(R.id.button_status_dialog_okay);
			cancelButton = (Button) findViewById(R.id.button_status_dialog_cancel);

			statusText.setText(status);

			okayButton.setOnClickListener(this);
			cancelButton.setOnClickListener(this);
		}

		public void onClick(View v) {
			if (v == cancelButton) {
				dismiss();
				finishActivity();
			} else if (v == okayButton) {
				status = statusText.getText().toString();
				if (status.equals("")) {
					AlertDialog alertDialog = new AlertDialog.Builder(
							getContext()).create();
					alertDialog.setTitle("Error");
					alertDialog.setMessage("Please enter some text.");
					alertDialog.setButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									finishActivity();
									dismiss();
									redisplayDialog();
								}
							});
					alertDialog.show();
				} else {
					saveStatus();
				}
			}
			dismiss();
		}

		@Override
		public void onBackPressed() {
			super.onBackPressed();
			context.onBackPressed();
		}
	}

	public void saveStatus() {
		transmittingIntent.putExtra("Status", status);

		setResult(Activity.RESULT_OK, transmittingIntent);
		finishActivity();
	}

	public void redisplayDialog() {
		setResult(CALL_AGAIN, transmittingIntent);
		finishActivity();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		this.finish();
	}

	public void finishActivity() {
		this.finish();
	}
}