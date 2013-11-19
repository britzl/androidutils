package se.springworks.android.utils.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

public class ScrollableBitmapView extends View {

	private static final float CLICKTHRESHOLD = 10;

	private enum State {
		NONE, DRAG, ZOOM
	}

	private State state = State.NONE;

	private Bitmap image;

	private ScaleGestureDetector scaleGestureDetector;
	private GestureDetector gestureDetector;

	private float maxScale = 2.0f;
	private float minScale = 0.5f;

	private final PointF touchDown = new PointF();
	private final PointF lastTouch = new PointF();
	private final PointF zoomCenter = new PointF();

	private final Matrix matrix = new Matrix();
	private final float[] m = new float[9];

	private Paint bitmapPaint = new Paint();

	public ScrollableBitmapView(Context context) {
		super(context);
		init(context);
	}

	public ScrollableBitmapView(Context context, AttributeSet attributeSet) {
		super(context, attributeSet);
		init(context);
	}

	private void init(Context context) {
		bitmapPaint.setFilterBitmap(true);
		
		scaleGestureDetector = new ScaleGestureDetector(context,
			new ScaleGestureDetector.SimpleOnScaleGestureListener() {
				private float previousScaleFactor;

				@Override
				public boolean onScaleBegin(ScaleGestureDetector detector) {
					zoomCenter.set(detector.getFocusX(), detector.getFocusY());
					state = State.ZOOM;
					previousScaleFactor = detector.getScaleFactor();
					invalidate();
					return true;
				}

				@Override
				public void onScaleEnd(ScaleGestureDetector detector) {
					state = State.NONE;
					limitOffset();
					invalidate();
				}

				@Override
				public boolean onScale(ScaleGestureDetector detector) {
					final float currentScale = getScale();
					final float scaleFactor = detector.getScaleFactor();
					float scaleRatio = (scaleFactor / previousScaleFactor);
					float newScale = currentScale * scaleRatio;
					// keep scale within min and max
					if (newScale > maxScale) {
						scaleRatio = maxScale / currentScale;
					}
					else if (newScale < minScale) {
						scaleRatio = minScale / currentScale;
					}
					previousScaleFactor = scaleFactor;
					matrix.postScale(scaleRatio, scaleRatio, detector.getFocusX(), detector.getFocusY());
					invalidate();
					return false;
				}
			});
		
		gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
			@Override
			public boolean onSingleTapUp(MotionEvent e) {
				state = State.NONE;
				invalidate();
				return true;
			}
			
			@Override
			public boolean onDown(MotionEvent e) {
				final float x = e.getX();
				final float y = e.getY();
				state = State.NONE;
				touchDown.set(x, y);
				lastTouch.set(x, y);	
				return true;
			}
			
			@Override
			public boolean onScroll(MotionEvent initial, MotionEvent current, float dx, float dy) {
				final float x = current.getX();
				final float y = current.getY();
				boolean distanceAboveThreshold = Math.abs(x - touchDown.x) > CLICKTHRESHOLD || Math.abs(y - touchDown.y) > CLICKTHRESHOLD;
				lastTouch.set(x, y);
				if(state == State.NONE && distanceAboveThreshold) {
					state = State.DRAG;
				}
				
				if (state == State.DRAG) {
					matrix.postTranslate(-dx, -dy);
					limitOffset();
					invalidate();
				}
				return true;
			}
		}); 
	}

	public void setMaxZoom(float max) {
		maxScale = max;
	}

	public void setMinZoom(float min) {
		minScale = min;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int width = MeasureSpec.getSize(widthMeasureSpec);
		int height = MeasureSpec.getSize(heightMeasureSpec);
		setMeasuredDimension(width, height);
	}
	
	public void destroy() {
		if(image != null) {
			image.recycle();
			image = null;
		}
	}

	public Bitmap getImage() {
		return image;
	}

	public void setImage(Bitmap image) {
		this.image = image;
		invalidate();
	}

	public void setImageBitmap(Bitmap bitmap) {
		image = bitmap;
		invalidate();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		super.onTouchEvent(event);
		scaleGestureDetector.onTouchEvent(event);
		return gestureDetector.onTouchEvent(event);
	}
	
	public boolean isDragging() {
		return state == State.DRAG;
	}
	
	public boolean isZooming() {
		return state == State.ZOOM;
	}

	protected void limitOffset() {		
		if(image == null) {
			return;
		}
		final float ox = getHorizontalOffset();
		final float oy = getVerticalOffset();
		final float scale = getScale();
		final float scrw = getWidth();
		final float scrh = getHeight();
		final float bmpw = image.getWidth() * scale;
		final float bmph = image.getHeight() * scale;
		final boolean screenIsWider = scrw >= bmpw;
		final boolean screenIsHigher = scrh >= bmph;
		
		float dx = 0;
		float leftedge = getPaddingLeft();
		float rightedge = -(bmpw + getPaddingRight() - scrw);
		// center horizontally if screen is wider than bitmap
		if(screenIsWider) {
			leftedge += (scrw - bmpw) / 2;
			rightedge -= (scrw - bmpw) / 2;
		}
		if(ox > leftedge) {
			dx = ox - leftedge;
		}
		else if(ox < rightedge) {
			dx = ox - rightedge;
		}
		
		float dy = 0;
		float topedge = getPaddingTop();
		float bottomedge = -(bmph + getPaddingBottom() - scrh);
		// center vertically if screen is higher than bitmap
		if(screenIsHigher) {
			topedge += (scrh - bmph) / 2;
			bottomedge -= (scrh - bmph) / 2;
		}
		if(oy > topedge) {
			dy = oy - topedge; 
		}
		else if(oy < bottomedge) {
			dy = oy - bottomedge;
		}

		matrix.postTranslate(-dx, -dy);
	}

	public float getMaxScale() {
		return maxScale;
	}

	public float getMinScale() {
		return minScale;
	}

	public float getScale() {
		matrix.getValues(m);
		return m[Matrix.MSCALE_X];
	}

	public float getHorizontalOffset() {
		matrix.getValues(m);
		return m[Matrix.MTRANS_X];
	}

	public float getVerticalOffset() {
		matrix.getValues(m);
		return m[Matrix.MTRANS_Y];
	}

	private final RectF bounds = new RectF();
	public RectF getBounds() {
		bounds.left = -getHorizontalOffset();
		bounds.top = -getVerticalOffset();
		bounds.right = bounds.left + getMeasuredWidth();
		bounds.bottom = bounds.top + getMeasuredHeight();
		return bounds;
	}


	@Override
	protected void onDraw(Canvas canvas) {
		if (image == null) {
			return;
		}
		canvas.drawBitmap(image, matrix, bitmapPaint);
	}

	public void zoomTo(float zoom, float x, float y) {
		float midx = (getWidth() / 2) / zoom;
		float midy = (getHeight() / 2) / zoom;
		matrix.reset();
		matrix.preTranslate(-(x - midx), -(y - midy));
		matrix.postScale(zoom, zoom);
	}
	
	public void moveTo(float x, float y) {
		float zoom = getScale();
		float midx = (getWidth() / 2) / zoom;
		float midy = (getHeight() / 2) / zoom;
		matrix.reset();
		matrix.preTranslate(-(x - midx), -(y - midy));
		matrix.postScale(zoom, zoom);
	}
	
	public void move(float dx, float dy) {
		float zoom = getScale();
		float x = -getHorizontalOffset() / zoom;
		float y = -getVerticalOffset() / zoom;
		matrix.reset();
		matrix.preTranslate(-(x + dx), -(y + dy));
		matrix.postScale(zoom, zoom);
	}

	
	public void zoomToFit() {
		float iw = image.getWidth();
		float ih = image.getHeight();
		float w = getWidth();
		float h = getHeight();
		float zoom = Math.min(w / iw, h / ih);
		zoomTo(zoom, iw / 2, ih / 2);
	}
}