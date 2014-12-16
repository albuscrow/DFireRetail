package com.dfire.retail.app.manage.activity.stockmanager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import com.dfire.retail.app.manage.adapter.AdjustmentReasonAdapter;
import com.dfire.retail.app.manage.data.StockAdjustVo;
import com.dfire.retail.app.manage.data.bo.StockAdjustReasonBo;
import com.dfire.retail.app.manage.global.Constants;
import com.dfire.retail.app.manage.network.AsyncHttpPost;
import com.dfire.retail.app.manage.network.AsyncHttpPost.RequestCallback;
import com.dfire.retail.app.manage.network.RequestParameter;
/**
 * 库存调整原因
 * @author ys
 *
 */
public class AdjustmentReasonActivity extends TitleActivity implements OnClickListener,OnItemClickListener{

	private ListView reasonListview;
	
	private String typeVal,typeName;
	
	private ImageButton minus;
	
	private StockAdjustVo adjustVo;
	
	private AdjustmentReasonAdapter adjustmentReasonAdapter;
	
	private List<Map<String, String>> typeList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_adjustment_reason_item);
		showBackbtn();
		setTitleText("调整原因");
		this.findView();
		this.getReason();
	}
	
	private void findView(){
		Bundle b = getIntent().getExtras();
		adjustVo=(StockAdjustVo)b.getSerializable("adjustVo");
		if (adjustVo!=null) {
			typeVal = adjustVo.getAdjustReasonId();
		}
		typeList = new ArrayList<Map<String, String>>();
		reasonListview = (ListView)findViewById(R.id.reasonListview);
		
		adjustmentReasonAdapter = new AdjustmentReasonAdapter(AdjustmentReasonActivity.this, typeList,typeVal);
		reasonListview.setAdapter(adjustmentReasonAdapter);
		reasonListview.setOnItemClickListener(AdjustmentReasonActivity.this);
		reasonListview.setDivider(null);
		
		minus = (ImageButton) findViewById(R.id.minus);
		minus.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.minus:
			//添加调整原因
			Intent addIntent = new Intent(AdjustmentReasonActivity.this,AdjustmentReasonAddActivity.class);
			startActivity(addIntent);
			break;
		default:
			break;
		}
	}
	@Override
	protected void onRestart() {
		super.onRestart();
		getReason();
	}
	/**
	 * 查询调整原因
	 */
	private void getReason(){
		RequestParameter params = new RequestParameter(true);
		params.setUrl(Constants.BASE_URL + "stockAdjust/getReason");
		new AsyncHttpPost(this, params, StockAdjustReasonBo.class, new RequestCallback() {
			@Override
			public void onSuccess(Object oj) {
				StockAdjustReasonBo bo = (StockAdjustReasonBo)oj;
				if (bo!=null) {
					List<Map<String, String>> types = bo.getListMap();
					if (types.size()>0) {
						typeList.clear();
					}
					typeList.addAll(types);
					adjustmentReasonAdapter.notifyDataSetChanged();
				}
			}
			@Override
			public void onFail(Exception e) {
				// TODO Auto-generated method stub
			}
		}).execute();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		typeVal = (String) typeList.get(position).get("typeVal");
		typeName = (String) typeList.get(position).get("typeName");
		adjustmentReasonAdapter.setTypeVal(typeVal);
		adjustmentReasonAdapter.notifyDataSetChanged();
		
		Intent select = new Intent(AdjustmentReasonActivity.this,StockGoodInfoActivity.class);
		select.putExtra("typeVal", typeVal);
		select.putExtra("typeName", typeName);
		setResult(100, select);
		finish();
	}
}
