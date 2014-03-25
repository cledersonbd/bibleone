package com.cdotti.bibleone;

import java.lang.reflect.Field;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;

public class StickyHeaderListView extends ListView {
	
	android.view.ViewGroup.LayoutParams lpForChapter;
	
	interface LifeCycleListener {
		void onDispatchDrawHappened(Canvas canvas);
	}
	
	private LifeCycleListener mLifeListener;
	private int mTopClipLength;
	private Rect mSelRect = new Rect();
	private Field mSelRectPosField;
	private boolean mClipToPad = true;	
	
	public StickyHeaderListView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public StickyHeaderListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
	public StickyHeaderListView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
		int desiredWidth = 100;
		int desiredHeight = 100;
		
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(widthMeasureSpec);
		int heightSize = MeasureSpec.getSize(widthMeasureSpec);
		
		int width = 0;
		int height = 0;
		
		if (widthMode == MeasureSpec.EXACTLY) {
			// Must be this size
			width = widthSize;
		} else if (widthMode == MeasureSpec.AT_MOST) {
			// Can't be bigger than
			width = Math.min(desiredWidth, widthSize);
		} else {
			width = desiredWidth;
			
		}
		
		if (heightMode == MeasureSpec.EXACTLY) {
			// Must be this size
			height = heightSize;
		} else if (heightMode == MeasureSpec.AT_MOST) {
			// Can't be bigger than
			height = Math.min(desiredHeight, heightSize);
		} else {
			height = desiredHeight;
		}
		
		setMeasuredDimension(width, height);
	}
	
	@SuppressLint("NewApi")
	@Override
	protected void layoutChildren() {
		// TODO Auto-generated method stub
		super.layoutChildren();
		
		int numChildren = getChildCount();
		
		for (int i = 0; i < numChildren; i++) {
			View sonView = getChildAt(i);
			// Se nao e um capitulo, layout diferente
			if (sonView.findViewById(R.id.lblVerseListHeaderChapterNum) == null) {
				android.view.ViewGroup.LayoutParams lp = sonView.getLayoutParams();
				sonView.setAlpha((float) 0.5);
			}
				
		}
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		int nI;
		int firstVisibble = getFirstVisiblePosition();
		
		View child = getChildAt(firstVisibble);
		if (child.findViewById(R.id.lblVerseListHeaderChapterNum) != null) {
			child.setLayoutParams(child.getLayoutParams());
		}
		super.dispatchDraw(canvas);
		
	}
}
