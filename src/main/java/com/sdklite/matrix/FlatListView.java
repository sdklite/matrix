package com.sdklite.matrix;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * @author johnsonlee
 */
public class FlatListView extends ListView {

    public FlatListView(final Context context) {
        this(context, null);
    }

    public FlatListView(final Context context, final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlatListView(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        final int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
