package com.dfire.retail.app.common.view;

import android.content.Context;
import android.util.AttributeSet;

public class StickyListHeadersListView extends com.handmark.pulltorefresh.listview.StickyListHeadersListView {

    public StickyListHeadersListView(Context context) {
        super(context);  
    }
    public StickyListHeadersListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StickyListHeadersListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    /**
     * 重写该方法，达到使ListView适应ScrollView的效果
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
        MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    } 
   
}
