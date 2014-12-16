package com.dfire.retail.app.manage.activity.logisticmanager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.activity.TitleActivity;
import com.dfire.retail.app.manage.adapter.ISingleChecksAdapter_New;
import com.dfire.retail.app.manage.data.DicVo;
import com.dfire.retail.app.manage.data.bo.StockAdjustReasonBo;
import com.dfire.retail.app.manage.global.Constants;
import com.dfire.retail.app.manage.network.AsyncHttpPost;
import com.dfire.retail.app.manage.network.AsyncHttpPost.RequestCallback;
import com.dfire.retail.app.manage.network.RequestParameter;

/**
 * 单选项页面.
 * 
 */
public class SupplyTypeSelectActivity extends TitleActivity implements View.OnClickListener {

	private ListView listView;

	private ImageButton addTypeBtn;

	private DicVo dicVo;

	private ISingleChecksAdapter_New singleChecksAdapter_New;

	private List<Map<String,String>> types = new ArrayList<Map<String,String>>();

	private int positionFlag;

	private String typeVal,typeName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_supply_type_select_view);
		findView();
		showBackbtn();
		setTitleText("类别");
	}

	public void findView() {
		Bundle b = getIntent().getExtras();
		dicVo=(DicVo)b.getSerializable("dicVo");
		if (dicVo!=null) {
			typeVal = String.valueOf(dicVo.getVal());
			typeName = dicVo.getName();
		}
		types = new ArrayList<Map<String,String>>();
		listView = (ListView) findViewById(R.id.checkList);
		addTypeBtn = (ImageButton) findViewById(R.id.add_type);
		addTypeBtn.setOnClickListener(this);
		singleChecksAdapter_New = new ISingleChecksAdapter_New(SupplyTypeSelectActivity.this, types,typeVal);
		listView.setAdapter(singleChecksAdapter_New);
		listView.setOnItemClickListener(new ItemClickListener());
		listView.setDivider(null);
		getSupplyType();
	}
	@Override
	protected void onRestart() {
		super.onRestart();
		getSupplyType();
	}
	private void getSupplyType() {
		RequestParameter params = new RequestParameter(true);
		params.setUrl(Constants.SUPPLY_INFO_MANAGE_GET_TYPE);
		new AsyncHttpPost(this, params, StockAdjustReasonBo.class, new RequestCallback() {
			
			@Override
			public void onSuccess(Object oj) {
				StockAdjustReasonBo bo = (StockAdjustReasonBo)oj;
				if (bo!=null) {
					types = bo.getListMap();
					singleChecksAdapter_New.initWithData(types, typeVal);
					singleChecksAdapter_New.notifyDataSetChanged();
				}
			}
			@Override
			public void onFail(Exception e) {
				// TODO Auto-generated method stub
			}
		}).execute();
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.add_type:
			Intent supplyTypeAdd = new Intent(SupplyTypeSelectActivity.this,SupplyTypeAddActivity.class);
			startActivityForResult(supplyTypeAdd, 100);
			break;
		default:
			break;
		}
	}

	private final class ItemClickListener implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> adpterView, View view,
				int position, long id) {
			typeVal = String.valueOf(types.get(position).get("typeVal"));
			typeName = (String) types.get(position).get("typeName");
			singleChecksAdapter_New.setTypeVal(types.get(position).get("typeVal").toString(),positionFlag);
			singleChecksAdapter_New.notifyDataSetChanged();

			Intent intent = new Intent();
			intent.setClass(SupplyTypeSelectActivity.this,SupplyManagerAddActivity.class);
			intent.putExtra("typeVal", typeVal);
			intent.putExtra("typeName", typeName);
			setResult(100, intent);
			finish();
		}
	}

}
