package com.dfire.retail.app.manage.widget.spinner;

import java.util.List;

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.widget.spinner.AbstractSpinerAdapter.IOnItemSelectListener;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.PopupWindow;


/**
 * @date 2014-7-11
 * @author wangpeng
 * @class description 下拉选择效果
 * 
 */
public class SpinerPopWindow extends PopupWindow implements OnItemClickListener{

	private Context mContext;
	private ListView mListView;
	private NormalSpinerAdapter mAdapter;
	private IOnItemSelectListener mItemSelectListener;
	
	
	public SpinerPopWindow(Context context)
	{
		super(context);
		
		mContext = context;
		init();
	}
	
	
	public void setItemListener(IOnItemSelectListener listener){
		mItemSelectListener = listener;
	}

	
	private void init()
	{
		View view = LayoutInflater.from(mContext).inflate(R.layout.spiner_window_layout, null);
		setContentView(view);		
		setWidth(LayoutParams.WRAP_CONTENT);
		setHeight(LayoutParams.WRAP_CONTENT);
		
		setFocusable(true);
    	ColorDrawable dw = new ColorDrawable(0x00);
		setBackgroundDrawable(dw);
	
		mListView = (ListView) view.findViewById(R.id.listview);

		mAdapter = new NormalSpinerAdapter(mContext);	
		mListView.setAdapter(mAdapter);	
		mListView.setOnItemClickListener(this);
	}
	
	
	public void refreshData(List<?> list, int selIndex)
	{
		if (list != null && selIndex  != -1)
		{
			mAdapter.refreshData(list, selIndex);
		}
	}
	
	boolean needDismiss = true;


	/**
	 * @return the needDismiss
	 */
	public boolean isNeedDismiss() {
		return needDismiss;
	}


	/**
	 * @param needDismiss the needDismiss to set
	 */
	public void setNeedDismiss(boolean needDismiss) {
		this.needDismiss = needDismiss;
	}


	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int pos, long arg3) {
		if (needDismiss) {
			dismiss();
		}
		if (mItemSelectListener != null){
			mItemSelectListener.onItemClick(pos);
		}
	}

	

	
}
