package com.dfire.retail.app.manage.activity.logisticmanager;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.activity.TitleActivity;
import com.dfire.retail.app.manage.adapter.StoreReturnReasonAdapter;
import com.dfire.retail.app.manage.data.DicVo;
import com.dfire.retail.app.manage.data.bo.ReasonListBo;
import com.dfire.retail.app.manage.global.Constants;
import com.dfire.retail.app.manage.network.AsyncHttpPost;
import com.dfire.retail.app.manage.network.AsyncHttpPost.RequestCallback;
import com.dfire.retail.app.manage.network.RequestParameter;

/**
 * 退货原因
 * @author ys 2014-11-12
 */
public class StoreReturnGoodsReasonActivity extends TitleActivity implements OnClickListener,OnItemClickListener {

	private List<DicVo> dicVoList;
	
	private ListView reasonListview;
	
	private String typeVal,typeName;
	
	private ImageButton minus;
	
	private DicVo dicVo;
	
	private StoreReturnReasonAdapter storeReturnReasonAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.store_return_reason);
		showBackbtn();
		setTitleText("退货原因");
		initData();
		getReasonList();
	}

	private void initData() {
		dicVoList = new ArrayList<DicVo>();
		Bundle b = getIntent().getExtras();
		dicVo=(DicVo)b.getSerializable("dicVo");
		if (dicVo!=null) {
			typeVal = String.valueOf(dicVo.getVal());
		}
		reasonListview = (ListView)findViewById(R.id.reasonListview);
		
		storeReturnReasonAdapter = new StoreReturnReasonAdapter(StoreReturnGoodsReasonActivity.this, dicVoList,typeVal);
		reasonListview.setAdapter(storeReturnReasonAdapter);
		reasonListview.setOnItemClickListener(StoreReturnGoodsReasonActivity.this);
		reasonListview.setDivider(null);
		
		minus = (ImageButton) findViewById(R.id.minus);
		minus.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.minus:
			Intent retureReasonAdd = new Intent(StoreReturnGoodsReasonActivity.this, StoreReturnGoodsReasonAddActivity.class);
			startActivity(retureReasonAdd);
			break;
		default:
			break;
		}
	}
	@Override
	protected void onRestart() {
		super.onRestart();
		getReasonList();
	}
	/**获取退货原因列表*/
	public void getReasonList() {
		RequestParameter parameters = new RequestParameter(true);
		parameters.setUrl(Constants.RETURNGOODS_REASONLIST);
		new AsyncHttpPost(this, parameters, ReasonListBo.class, new RequestCallback() {
			
			@Override
			public void onSuccess(Object oj) {
				ReasonListBo bo = (ReasonListBo)oj;
				if (bo!=null) {
					List<DicVo> types = bo.getReturnResonList();
					if (types.size()>0) {
						dicVoList.clear();
					}
					dicVoList.addAll(types);
					storeReturnReasonAdapter.notifyDataSetChanged();
				}
			}
			
			@Override
			public void onFail(Exception e) {
			}
		}).execute();
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		typeVal = String.valueOf(dicVoList.get(position).getVal());
		typeName = (String) dicVoList.get(position).getName();
		storeReturnReasonAdapter.setTypeVal(typeVal);
		storeReturnReasonAdapter.notifyDataSetChanged();
		 
		Intent select = new Intent(StoreReturnGoodsReasonActivity.this,StoreReturnGoodsDetailItemActivity.class);
		select.putExtra("typeVal", typeVal);
		select.putExtra("typeName", typeName);
		setResult(100, select);
		finish();
	}
}
