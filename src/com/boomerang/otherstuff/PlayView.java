package com.boomerang.otherstuff;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;

import com.boomerang.gui.ImageSwipeScreenActivity;

public class PlayView extends View {

	private GestureDetector gestures;
	private View target;
	private Context context;

	public PlayView(ImageSwipeScreenActivity context, View target) {
		super(context);
		gestures = new GestureDetector(context, new GestureListener(this,
				context));
		this.target = target;
		this.context = context;
	}

	public void onAnimateMove(float dx, float dy) {
		Animation a = new TranslateAnimation(0.0f, dx * 1.5f, 0.0f, dy * 1.5f);
		a.setDuration(1750);
		a.setStartOffset(300);
		a.setInterpolator(AnimationUtils.loadInterpolator(context,
				android.R.anim.overshoot_interpolator));
		target.startAnimation(a);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return gestures.onTouchEvent(event);
	}
}
