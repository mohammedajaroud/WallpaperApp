package com.stefan.wallpaper.demo.views;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * PhotoPager class used for swiping images in full screen mode
 *
 * @author itcsform
 */
public class PhotoPager extends ViewPager {

    public PhotoPager(Context context) {
        super(context);
    }

    public PhotoPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            return super.onInterceptTouchEvent(ev);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return false;
        }
    }

}
