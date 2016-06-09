package com.sdklite.matrix;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * @author johnsonlee
 */
public class ScrollableViewPager extends ViewPager {

    private boolean scrollable;

    public ScrollableViewPager(final Context context) {
        this(context, null);
    }

    public ScrollableViewPager(final Context context, final AttributeSet attrs) {
        super(context, attrs);

        final TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ScrollableViewPager);
        this.scrollable = ta.getBoolean(R.styleable.ScrollableViewPager_scrollableViewPagerScrollable, false);
        ta.recycle();
    }

    public boolean isScrollable() {
        return this.scrollable;
    }

    public void setScrollable(final boolean scrollable) {
        this.scrollable = scrollable;
    }

    @Override
    public boolean onInterceptTouchEvent(final MotionEvent event) {
        if (this.scrollable) {
            return super.onInterceptTouchEvent(event);
        }

        return false;
    }
}
