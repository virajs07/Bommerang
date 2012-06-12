package com.boomerang.otherstuff;

import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;

import com.boomerang.gui.ImageSwipeScreenActivity;

public class GestureListener extends SimpleOnGestureListener {

	PlayView playView;
	ImageSwipeScreenActivity imageSwipeScreenActivity;
	private String type = null;

	public GestureListener(PlayView playView, ImageSwipeScreenActivity parent) {
		this.playView = playView;
		this.imageSwipeScreenActivity = parent;
		type = "Image";
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2,
			final float velocityX, final float velocityY) {
		boolean isDirectionClear = true;
		final float distanceTimeFactor = 0.4f;
		float totalDx = (distanceTimeFactor * velocityX / 2);
		float totalDy = (distanceTimeFactor * velocityY / 2);

		if (Math.abs(velocityY / velocityX) > 1.5f) {
			if (totalDy < 0) {
				// TODO Put code here
			} else {
				// TODO Put code here
			}
		} else if (Math.abs(velocityY / velocityX) < 0.85f) {
			if (totalDx > 0) {
				// TODO Put code here
			} else {
				// TODO Put code here
			}
		} else {
			// TODO Put code here
			isDirectionClear = false;
		}

		playView.onAnimateMove(totalDx, totalDy);
		return true;
	}

	@Override
	public boolean onDown(MotionEvent e) {
		return true;
	}

	@Override
	public boolean onDoubleTap(MotionEvent e) {
		return true;
	}
}