package bigappcompany.com.artistbooking.widgets;

import android.content.Context;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;



public class VerticalViewpager extends ViewPager {

    public VerticalViewpager(Context context) {
        super(context);
        init();
    }

    public VerticalViewpager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        // The majority of the magic happens here
        setPageTransformer(true, new VerticalPageTransformer());
        // The easiest way to get rid of the overscroll drawing that happens on the left and right
        setOverScrollMode(OVER_SCROLL_NEVER);
    }

    private class VerticalPageTransformer implements ViewPager.PageTransformer {
        //TODO:ADDED here
        public static final float MAX_SCALE = 1.00f;
        public static final float MIN_SCALE = 0.85f;
        //TODO:upto here
        @Override
        public void transformPage(View view, float position) {

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0);

            } else if (position <= 1) { // [-1,1]
                view.setAlpha(1);

                // Counteract the default slide transition
                view.setTranslationX(view.getWidth() * -position);

                //set Y position to swipe in from top
                float yPosition = position * view.getHeight();
                view.setTranslationY(yPosition);

                //TODO:adding code here

                position = position < -1 ? -1 : position;
                position = position > 1 ? 1 : position;

                float tempScale = position < 0 ? 1 + position : 1 - position;

                float slope = (MAX_SCALE - MIN_SCALE) / 1;
                float scaleValue = MIN_SCALE + tempScale * slope;
                view.setScaleX(scaleValue);
                view.setScaleY(scaleValue);
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                    view.getParent().requestLayout();
                }

                //TODO:upto here


            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0);
            }
        }
    }

    /**
     * Swaps the X and Y coordinates of your touch event.
     */
    private MotionEvent swapXY(MotionEvent ev) {
        float width = getWidth();
        float height = getHeight();

        float newX = (ev.getY() / height) * width;
        float newY = (ev.getX() / width) * height;

        ev.setLocation(newX, newY);

        return ev;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev){
        boolean intercepted = super.onInterceptTouchEvent(swapXY(ev));
        swapXY(ev); // return touch coordinates to original reference frame for any child views
        return intercepted;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return super.onTouchEvent(swapXY(ev));
    }

}
