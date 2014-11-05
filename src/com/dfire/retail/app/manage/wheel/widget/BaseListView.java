package com.dfire.retail.app.manage.wheel.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class BaseListView extends ListView
{

    public BaseListView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    public BaseListView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public BaseListView(Context context)
    {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);

        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
