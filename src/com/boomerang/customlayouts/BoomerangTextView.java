package com.boomerang.customlayouts;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import com.boomerang.gui.R;

public class BoomerangTextView extends View {

	private Paint textPaint;
	private String text;
	private int ascent;

	public BoomerangTextView(Context context) {
		super(context);
		initView();
	}

	public BoomerangTextView(Context context, AttributeSet attrs) {
		super(context, attrs);

		initView();

		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.BoomerangTextView);

		CharSequence s = a.getString(R.styleable.BoomerangTextView_text);
		if (s != null) {
			setText(s.toString());
		}

		setTextColor(a.getColor(R.styleable.BoomerangTextView_textColor,
				0xFF4F5A61));

		int textSize = a.getDimensionPixelOffset(
				R.styleable.BoomerangTextView_textSize, 0);
		if (textSize > 0) {
			setTextSize(textSize);
		}

		a.recycle();
	}

	private void initView() {
		textPaint = new Paint();
		textPaint.setAntiAlias(true);
		textPaint.setTextSize(16);
		textPaint.setColor(0xFF4F5A61);
		textPaint.setTypeface(Typeface.createFromAsset(
				getContext().getAssets(), "Fonts/RawengulkDemibold.otf"));
	}

	public void setText(String text) {
		this.text = text;
		requestLayout();
		invalidate();
	}

	public void setTextSize(int size) {
		textPaint.setTextSize(size);
		requestLayout();
		invalidate();
	}

	public void setTextColor(int color) {
		textPaint.setColor(color);
		invalidate();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMeasuredDimension(measureWidth(widthMeasureSpec),
				measureHeight(heightMeasureSpec));
	}

	private int measureWidth(int measureSpec) {
		int result = 0;
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);

		if (specMode == MeasureSpec.EXACTLY) {
			// We were told how big to be
			result = specSize;
		} else {
			// Measure the text
			result = (int) textPaint.measureText(text) + getPaddingLeft()
					+ getPaddingRight();
			if (specMode == MeasureSpec.AT_MOST) {
				// Respect AT_MOST value if that was what is called for by
				// measureSpec
				result = Math.min(result, specSize);
			}
		}

		return result;
	}

	private int measureHeight(int measureSpec) {
		int result = 0;
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);

		ascent = (int) textPaint.ascent();
		if (specMode == MeasureSpec.EXACTLY) {
			// We were told how big to be
			result = specSize;
		} else {
			// Measure the text (beware: ascent is a negative number)
			result = (int) (-ascent + textPaint.descent()) + getPaddingTop()
					+ getPaddingBottom();
			if (specMode == MeasureSpec.AT_MOST) {
				// Respect AT_MOST value if that was what is called for by
				// measureSpec
				result = Math.min(result, specSize);
			}
		}

		return result;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawText(text, getPaddingLeft(), getPaddingTop() - ascent,
				textPaint);
	}
}
