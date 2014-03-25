package com.cdotti.bibleone;

import android.content.Context;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class VerseListGestureListener implements OnTouchListener {

    private final GestureDetector gestureDetector;
    private Context mContext;
    private ListView mListView;
    
    public VerseListGestureListener(Context context, ListView list) {
    	mContext = context;
    	gestureDetector = new GestureDetector(mContext, new GestureListener());
    	mListView = list;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {    		
        return gestureDetector.onTouchEvent(motionEvent);
    }

    private final class GestureListener extends SimpleOnGestureListener {

        private static final int SWIPE_THRESHOLD = 100;

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
        	int firstPositionVisible = mListView.getFirstVisiblePosition();
        	int positionDetected = mListView.pointToPosition((int)e.getX(), (int)e.getY());
        	View v = mListView.getChildAt(positionDetected - firstPositionVisible);
        	ViewFlipper viewFlipper = (ViewFlipper) v.findViewById(R.id.viewFlipperVerseRow);
        	
        	if (viewFlipper != null) {
        		if (viewFlipper.getDisplayedChild() == 0) {
        			viewFlipper.setInAnimation(mContext, R.anim.activity_slide_in_from_right);
        			viewFlipper.setOutAnimation(mContext, R.anim.activity_slide_out_to_left);
        			viewFlipper.showNext();
        		}        			
        	}
        	return true;
        }
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            boolean result = false;
            try {
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();
                // Verifica somente o scroll horizontal
                if (Math.abs(diffX) > SWIPE_THRESHOLD) {
                    if (diffX > 0) {
                        onSwipeRight(e1.getX(), e1.getY());
                    } else {
                        onSwipeLeft(e1.getX(), e1.getY());
                    }
                } else if (Math.abs(diffY) > SWIPE_THRESHOLD) {
                        if (diffY > 0) {
                            //onSwipeBottom(e1.getX(), e1.getY());
                        } else {
                            //onSwipeTop(e1.getX(), e1.getY());
                        	//onSwipeTop(distanceX, distanceY);
                        }
                    }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return result;
        }
    }
    
    public void onSwipeRight() {
    	Toast.makeText(mContext, "SwipeRight", Toast.LENGTH_SHORT).show();
    }
    public void onSwipeRight(float x, float y) {
    	int firstPositionVisible = mListView.getFirstVisiblePosition();
    	int positionDetected = mListView.pointToPosition((int)x, (int)y);
    	View v = mListView.getChildAt(positionDetected - firstPositionVisible);
    	ViewFlipper viewFlipper = (ViewFlipper) v.findViewById(R.id.viewFlipperVerseRow);
    	
    	if (viewFlipper != null) {
    		if (viewFlipper.getDisplayedChild() == 1) {
    			viewFlipper.setInAnimation(mContext, R.anim.activity_slide_in_from_left);
    			viewFlipper.setOutAnimation(mContext, R.anim.activity_slide_out_to_right);
    			
    			viewFlipper.showPrevious();
    		}
    	}
    }
    public void onSwipeLeft() {
    	Toast.makeText(mContext, "SwipeLeft", Toast.LENGTH_SHORT).show();
    }
    public void onSwipeLeft(float x, float y) {
    	int firstPositionVisible = mListView.getFirstVisiblePosition();
    	int positionDetected = mListView.pointToPosition((int)x, (int)y);
    	View v = mListView.getChildAt(positionDetected - firstPositionVisible);
    	ViewFlipper viewFlipper = (ViewFlipper) v.findViewById(R.id.viewFlipperVerseRow);
    	
    	if (viewFlipper != null) {
    		if (viewFlipper.getDisplayedChild() == 0) {
    			viewFlipper.setInAnimation(mContext, R.anim.activity_slide_in_from_right);
    			viewFlipper.setOutAnimation(mContext, R.anim.activity_slide_out_to_left);
    			
    			viewFlipper.showNext();
    		}
    	}
    }
    public void onSwipeBottom(float x, float y) {
    	int firstPositionVisible = mListView.getFirstVisiblePosition();
    	for (int i = firstPositionVisible + 1; i < firstPositionVisible + 1 + mListView.getLastVisiblePosition(); i++) {
    		mListView.getChildAt(i).scrollBy((int) x, 0);
    		mListView.getChildAt(i).invalidate();
    	}
    }
    public void onSwipeTop(float x, float y) {
    	int firstPositionVisible = mListView.getFirstVisiblePosition();
    	for (int i = firstPositionVisible + 1; i < firstPositionVisible + 1 + mListView.getLastVisiblePosition(); i++) {
    		mListView.getChildAt(i).scrollBy(0, (int) y);
    		mListView.getChildAt(i).invalidate();
    	}
    }
}