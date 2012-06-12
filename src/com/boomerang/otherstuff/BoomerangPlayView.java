package com.boomerang.otherstuff;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;

public class BoomerangPlayView extends View {

	private GestureDetector gestures;
	private Matrix translate;
	private Bitmap image;

	public BoomerangPlayView(Context context) {
		super(context);
		translate = new Matrix();
		gestures = new GestureDetector(context, new BoomerangGestureListener(this,
				context));
		//TODO remove this comment :)
		//image = BitmapFactory.decodeResource(getResources(), R.drawable.boomerang_large);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawBitmap(image, translate, null);
	}

	public void onAnimateMove(float dx, float dy, long duration,
			final boolean isDirectionClear) {
		AnimationSet set = new AnimationSet(false);
		TranslateAnimation translate = new TranslateAnimation(-dx, 0, -dy, 0);
		RotateAnimation rotate = new RotateAnimation(0, 359,
				image.getWidth() / 2, image.getHeight() / 2);
		translate.setInterpolator(new DecelerateInterpolator());
		translate.setDuration(duration);
		rotate.setInterpolator(new AccelerateDecelerateInterpolator());
		rotate.setDuration(duration);

		set.addAnimation(translate);
		set.addAnimation(rotate);

		startAnimation(set);
		invalidate();
	}

	public void onMove(float dx, float dy) {
		translate.postTranslate(dx, dy);
	}

	public void onResetLocation() {
		translate.reset();
		invalidate();
	}

	public void onSetLocation(float dx, float dy) {
		translate.postTranslate(dx, dy);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return gestures.onTouchEvent(event);
	}
	
	public class BoomerangGestureListener extends SimpleOnGestureListener {

		BoomerangPlayView boomerangView;
		Context parent;

		public BoomerangGestureListener(BoomerangPlayView boomerangView,
				Context parent) {
			this.boomerangView = boomerangView;
			this.parent = parent;
		}

		@Override
		public boolean onDown(MotionEvent e) {
			return true;
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2,
				final float velocityX, final float velocityY) {

			boolean isDirectionClear = true;
			final float distanceTimeFactor = 0.4f;
			float totalDx = (distanceTimeFactor * velocityX / 2);
			float totalDy = (distanceTimeFactor * velocityY / 2);

			boomerangView.onAnimateMove(totalDx, totalDy,
					(long) (1000 * distanceTimeFactor), isDirectionClear);
			return true;
		}

		@Override
		public boolean onDoubleTap(MotionEvent e) {
			boomerangView.onResetLocation();
			return true;
		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
				float distanceY) {

			boomerangView.onMove(-distanceX, -distanceY);

			return true;
		}

	}
}
