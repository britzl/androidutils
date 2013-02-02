package se.springworks.android.utils.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

public class ScrollableBitmapView extends View {

	private static final float CLICKTHRESHOLD = 10;

	private static Logger logger = LoggerFactory.getLogger(ScrollableBitmapView.class);

	private enum State {
		NONE, DRAG, ZOOM
	}

	private State state = State.NONE;

	private Bitmap image;

	private ScaleGestureDetector scaleGestureDetector;

	private float maxScale = 2.0f;
	private float minScale = 0.5f;

	private final PointF touchDown = new PointF();
	private final PointF lastTouch = new PointF();
	private final PointF zoomCenter = new PointF();

	private final Matrix matrix = new Matrix();
	private final float[] m = new float[9];

	// debugtext
	private Paint textpaint = new Paint();
	
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
		textpaint.setTextSize(15);
		textpaint.setColor(0xFFFF0000);
		
		bitmapPaint.setFilterBitmap(true);
		
		scaleGestureDetector = new ScaleGestureDetector(context,
				new ScaleGestureDetector.SimpleOnScaleGestureListener() {
					private float previousScaleFactor;

					@Override
					public boolean onScaleBegin(ScaleGestureDetector detector) {
						logger.debug("onScaleBegin()");
						zoomCenter.set(detector.getFocusX(), detector.getFocusY());
						state = State.ZOOM;
						previousScaleFactor = detector.getScaleFactor();
						invalidate();
						return true;
					}

					@Override
					public void onScaleEnd(ScaleGestureDetector detector) {
						logger.debug("onScaleEnd()");
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

	public Bitmap getImage() {
		return image;
	}

	public void setImage(Bitmap image) {
		this.image = image;
	}

	public void setImageBitmap(Bitmap bitmap) {
		image = bitmap;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		super.onTouchEvent(event);
		scaleGestureDetector.onTouchEvent(event);
		final float x = event.getX();
		final float y = event.getY();

		boolean distanceAboveThreshold = Math.abs(x - touchDown.x) > CLICKTHRESHOLD || Math.abs(y - touchDown.y) > CLICKTHRESHOLD;
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			logger.debug("down");
			state = State.NONE;
			touchDown.set(x, y);
			lastTouch.set(x, y);
			return true;
		}
		else if (event.getAction() == MotionEvent.ACTION_MOVE) {
			logger.debug("move");
			final float deltaX = x - lastTouch.x;
			final float deltaY = y - lastTouch.y;
			lastTouch.set(x, y);
			if(state == State.NONE && distanceAboveThreshold) {
				state = State.DRAG;
			}
			
			if (state == State.DRAG) {
				matrix.postTranslate(deltaX, deltaY);
				limitOffset();
				logger.debug("ho = " + getHorizontalOffset());
				invalidate();
			}
		}
		else if (event.getAction() == MotionEvent.ACTION_UP) {
			state = State.NONE;
			invalidate();
		}
		logger.debug("state = " + state);
		return super.onTouchEvent(event);
	}
	
	public boolean isDragging() {
		return state == State.DRAG;
	}
	
	public boolean isZooming() {
		return state == State.ZOOM;
	}

	private void limitOffset() {		
		final float ox = getHorizontalOffset();
		final float oy = getVerticalOffset();
		final float scale = getScale();
		final float scrw = getWidth();
		final float scrh = getHeight();
		final float bmpw = image.getWidth() * scale;
		final float bmph = image.getHeight() * scale;
		
		float dx = 0;
		float dy = 0;
		float leftedge = getPaddingLeft();
		float rightedge = -(bmpw + getPaddingRight() - scrw);
		if(ox > leftedge) {
			dx = ox - leftedge;
		}
		else if(ox < rightedge) {
			dx = ox - rightedge;
		}
		
		float topedge = getPaddingTop();
		float bottomedge = -(bmph + getPaddingBottom() - scrh);
		if(oy > topedge) {
			dy = oy - topedge; 
		}
		else if(oy < bottomedge) {
			dy = oy - bottomedge;
		}
		
		matrix.postTranslate(-dx, -dy);
	}

	protected void onSingleTap(float x, float y) {
		// override
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
//		canvas.drawText("zox = " + zoomCenter.x + " hox = " + getHorizontalOffset(), 10, 30, textpaint);
//		canvas.drawText("zoy = " + zoomCenter.y + " hoy = " + getVerticalOffset(), 10, 50, textpaint);
//		canvas.drawText("scale = " + getScale() + " w = " + getWidth() + " h = " + getHeight(), 10, 70, textpaint);
//		canvas.drawText("padding = " + getPaddingLeft() + "," + getPaddingTop() + "," + getPaddingRight() + "," + getPaddingBottom(), 10, 90, textpaint);
	}

	public void zoomTo(float zoom, float x, float y) {
		float midx = (getWidth() / 2) / zoom;
		float midy = (getHeight() / 2) / zoom;
		matrix.reset();
		matrix.preTranslate(-(x - midx), -(y - midy));
		matrix.postScale(zoom, zoom);
	}
	
	public void zoomToFit() {
		float iw = image.getWidth();
		float ih = image.getHeight();
		float w = getWidth();
		float h = getHeight();
		float zoom = Math.min(w / iw, h / ih);
		logger.debug("zoomToFit() zoom = " + zoom);
		zoomTo(zoom, iw / 2, ih / 2);
	}
}