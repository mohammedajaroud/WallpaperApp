package com.stefan.wallpaper.demo.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.stefan.wallpaper.demo.R;

public class ScaledImageView extends ImageView {

    private float widthRatio = 0.5f;

    public ScaledImageView(Context context) {
        super(context);
    }

    public ScaledImageView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.ScaledImageView);
        final int N = a.getIndexCount();
        for (int i = 0; i < N; ++i) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.ScaledImageView_image_view_ratio:
                    float styleableRatio = a.getFloat(attr, widthRatio);
                    widthRatio = styleableRatio;
                    break;
            }
        }
        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(),
                (int) (getMeasuredWidth() * widthRatio));
    }
}