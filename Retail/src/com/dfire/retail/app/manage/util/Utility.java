package com.dfire.retail.app.manage.util;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.handmark.pulltorefresh.listview.PullToRefreshListView;



public class Utility {

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) { // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            if (listItem instanceof RelativeLayout) {
                listItem.setLayoutParams(new ListView.LayoutParams(ListView.LayoutParams.WRAP_CONTENT, ListView.LayoutParams.WRAP_CONTENT));//对于子item是RelativeLayout, 防止其出现空指针.
            }
            listItem.measure(0, 0); // 计算子项View 的宽度
            totalHeight += listItem.getMeasuredHeight(); // 统计子项的高度
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高高度
        // params.height�?��得到整个ListView完整显示�?��的高�
        listView.setLayoutParams(params);
    }
    
    public static void setListViewHeightBasedOnChildren(PullToRefreshListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getRefreshableView().getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) { // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            if (listItem instanceof RelativeLayout) {
                listItem.setLayoutParams(new ListView.LayoutParams(ListView.LayoutParams.WRAP_CONTENT, ListView.LayoutParams.WRAP_CONTENT));//对于子item是RelativeLayout, 防止其出现空指针.
            }
            listItem.measure(0, 0); // 计算子项View 的宽度
            totalHeight += listItem.getMeasuredHeight(); // 统计子项的高度
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getRefreshableView().getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高高度
        // params.height�?��得到整个ListView完整显示�?��的高�
        listView.setLayoutParams(params);
    }

}