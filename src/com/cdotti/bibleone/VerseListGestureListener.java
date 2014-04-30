package com.cdotti.bibleone;

import java.util.ArrayList;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;
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
    private ArrayList<ViewFlipper> mUsedIndexes;
    
    public VerseListGestureListener(Context context) {
    	mContext = context;
    	gestureDetector = new GestureDetector(mContext, new GestureListener());
    	mUsedIndexes = new ArrayList<ViewFlipper>();
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
    	if (view instanceof StickyListHeadersListView && mListView == null)
    		mListView = (ListView) ((StickyListHeadersListView) view).getWrappedList();
    	
        return gestureDetector.onTouchEvent(motionEvent);
    }

    private final class GestureListener extends SimpleOnGestureListener {
    	
        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_THRESHOLD_Y = 50;

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
        			mUsedIndexes.add(viewFlipper);
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
                        result = onSwipeRight(e1.getX(), e1.getY());
                    } else {
                        result = onSwipeLeft(e1.getX(), e1.getY());
                    }
                } else if (Math.abs(diffY) > SWIPE_THRESHOLD_Y) {
                        if (diffY > 0) {
                            //onSwipeBottom(e1.getX(), e1.getY());
                        } else {
                            //onSwipeTop(e1.getX(), e1.getY());
                        	//onSwipeTop(distanceX, distanceY);
                        }
                        
                        resetFlipper();
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
    public boolean onSwipeRight(float x, float y) {
    	boolean consumed = false;
    	int firstPositionVisible = mListView.getFirstVisiblePosition();
    	int positionDetected = mListView.pointToPosition((int)x, (int)y);
    	View v = mListView.getChildAt(positionDetected - firstPositionVisible);
    	ViewFlipper viewFlipper = (ViewFlipper) v.findViewById(R.id.viewFlipperVerseRow);
    	
    	if (viewFlipper != null) {
    		if (viewFlipper.getDisplayedChild() == 1) {
    			viewFlipper.setInAnimation(mContext, R.anim.activity_slide_in_from_left);
    			viewFlipper.setOutAnimation(mContext, R.anim.activity_slide_out_to_right);
    			
    			viewFlipper.showPrevious();
    			mUsedIndexes.remove(viewFlipper);
    		}
    		// Evento consumido, evita a propagacao
    		consumed = true;
    	}

    	return consumed;
    }
    public void onSwipeLeft() {
    	Toast.makeText(mContext, "SwipeLeft", Toast.LENGTH_SHORT).show();
    }
    public boolean onSwipeLeft(float x, float y) {
    	boolean consumed = false;
    	int firstPositionVisible = mListView.getFirstVisiblePosition();
    	int positionDetected = mListView.pointToPosition((int)x, (int)y);
    	View v = mListView.getChildAt(positionDetected - firstPositionVisible);
    	ViewFlipper viewFlipper = (ViewFlipper) v.findViewById(R.id.viewFlipperVerseRow);
    	
    	if (viewFlipper != null) {
    		if (viewFlipper.getDisplayedChild() == 0) {
    			viewFlipper.setInAnimation(mContext, R.anim.activity_slide_in_from_right);
    			viewFlipper.setOutAnimation(mContext, R.anim.activity_slide_out_to_left);
    			
    			viewFlipper.showNext();
    			mUsedIndexes.add(viewFlipper);
    		}
    		// Evento consumido, evita a propagacao
    		consumed = true;
    	}

    	return consumed;
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
    
    private void resetFlipper() {
    	ViewFlipper vf;    	
    	for (int i = 0; i < mUsedIndexes.size(); i++) {
    		vf = mUsedIndexes.get(i);
    		vf.setInAnimation(mContext, R.anim.activity_slide_in_from_left);
    		vf.setOutAnimation(mContext, R.anim.activity_slide_out_to_right);
    		vf.setDisplayedChild(0);
    	}
    	mUsedIndexes.clear();
    }
}