package com.example.zhouwei.comments;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by zhouwei on 2018/12/14.
 */

public class CompatiableListView extends ListView {
    public CompatiableListView(Context context) {
        super(context);
    }

    public CompatiableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CompatiableListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
