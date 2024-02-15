package com.solfege.solfege;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class PitchView extends View {
	private float m_centerPitch, m_currentPitch;
	private int viewWidth, viewHeight;
	private final Paint paint = new Paint();
	private float pitch = 0;
	private float m_curPitchDiff = -63.5f;
	private float m_prevPitchDiff = m_curPitchDiff;
	private double m_phi = -Math.PI / 4;
	private int m_viewCenter;
	private RectF mRectF;
    Path myPath1 = new Path();

	public PitchView(Context context) {
		super(context);
		pitch = 0;
		m_prevPitchDiff = 1;
		m_phi = -Math.PI / 4;
	}

	public PitchView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public PitchView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void setCenterPitch(float centerPitch) {
		m_centerPitch = centerPitch;
		invalidate();
	}

	public void setCurrentPitch(float currentPitch) {
		m_currentPitch = currentPitch;
		invalidate();
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		viewWidth = w;
		viewHeight = h;
	}

	@Override
	protected void onDraw(Canvas canvas) {

		m_viewCenter = viewWidth / 2;

		//draw center green line
		paint.setStrokeWidth(6.0f);	//width of center, green line
		paint.setColor(0xFF8FD600); //green
		
		canvas.drawLine(m_viewCenter, 0, m_viewCenter, viewHeight, paint);
				
		float pitchDiff = (m_currentPitch - m_centerPitch) / 2;
		
		//if pitchDiff/2 is < within 1 midi note, draw blue line, otherwise draw red line
		if (-1 < pitchDiff && pitchDiff < 1) {
			paint.setStrokeWidth(4.0f); 
			paint.setColor(0xFF4700D6);
			paint.setAntiAlias(true);
			
		} else {
			paint.setStrokeWidth(8.0f); 
			paint.setColor(0xFFFF3366);
			pitchDiff = (pitchDiff < 0) ? -1 : 1;
			paint.setAntiAlias(false);
		}
//		paint.setShadowLayer(100, 100, 100, 0xffffffff); //doesn't work
		
		double phi = pitchDiff * Math.PI / 4;
		
		canvas.drawLine(m_viewCenter, viewHeight, m_viewCenter + (float) Math.sin(phi) * viewHeight, viewHeight - (float) Math.cos(phi) * viewHeight, paint);
		
		
		//draw 2 black lines around the meter thing
	    paint.setStyle(Style.STROKE);
		paint.setStrokeWidth(1.5f); 
		paint.setColor(Color.GRAY);
		phi = - Math.PI / 4;
		canvas.drawLine(m_viewCenter, viewHeight, m_viewCenter + (float) Math.sin(phi) * viewHeight, viewHeight - (float) Math.cos(phi) * viewHeight, paint);
		phi =  Math.PI / 4;
		canvas.drawLine(m_viewCenter, viewHeight, m_viewCenter + (float) Math.sin(phi) * viewHeight, viewHeight - (float) Math.cos(phi) * viewHeight, paint);
		
		//draw arc between 2 black lines
//		paint.setColor(0x66000000);
//		mRectF = new RectF(0, 0, viewWidth, 2*viewHeight);
//		canvas.drawArc (mRectF, 225, 90, false, paint);
		
		//TO TRY, NOT SURE WHAT W AND H ARE THOUGH

	    myPath1.moveTo(m_viewCenter - (float) Math.sin(Math.PI / 4) * viewHeight, viewHeight - (float) Math.cos(Math.PI / 4) * viewHeight);
	    PointF mPoint1 = new PointF(m_viewCenter , -85);
	    PointF mPoint2 = new PointF(m_viewCenter + (float) Math.sin(Math.PI / 4) * viewHeight, viewHeight - (float) Math.cos(Math.PI / 4) * viewHeight);
	    myPath1.quadTo(mPoint1.x, mPoint1.y, mPoint2.x, mPoint2.y);

	    paint.setAntiAlias(true);
	    canvas.drawPath(myPath1, paint);
	    paint.setAntiAlias(false);
		
//		m_viewCenter = viewWidth / 2;
//
//		//draw center green line
//		paint.setStrokeWidth(6.0f);	//width of center, green line
//		paint.setColor(0xFF8FD600); //green
//		canvas.drawLine(m_viewCenter, 0, m_viewCenter, viewHeight, paint);
//		
//		//prepare current pitch line
//		paint.setStrokeWidth(6.0f); 
//		paint.setColor(0xFFFF3366);
//
//		//calculate current pitch difference
//		m_curPitchDiff = (float) ( (Math.floor(m_currentPitch * 100) / 100)  - (Math.floor(m_centerPitch * 100) / 100)) / 2;
//
//		//if difference in pitch is increasing
//		if (m_curPitchDiff > m_prevPitchDiff){
//			if (m_phi < Math.PI / 4)
//				m_phi += Math.PI /20;
//			
//		//if difference in pitch is decreasing
//		} else if (m_curPitchDiff < m_prevPitchDiff){
//			if (m_phi > -Math.PI / 4)
//				m_phi -= Math.PI /20;
//		}
//
//		canvas.drawLine(m_viewCenter, viewHeight, m_viewCenter + (float) Math.sin(m_phi) * viewHeight * 0.9f, viewHeight - (float) Math.cos(m_phi) * viewHeight * 0.9f, paint);
//		m_prevPitchDiff = m_curPitchDiff;
//		invalidate();
	}



	
}
