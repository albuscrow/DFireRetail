package com.dfire.retail.app.manage.activity.stockmanager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.activity.logisticmanager.SelectShopActivity;
import com.dfire.retail.app.manage.activity.logisticmanager.StoreOrderActivity;
import com.dfire.retail.app.manage.adapter.AdjustmentReasonAdapter;
import com.dfire.retail.app.manage.data.SearchGoodsVo;
import com.dfire.retail.app.manage.data.StockAdjustVo;
import com.dfire.retail.app.manage.global.Constants;
import com.dfire.retail.app.manage.network.AsyncHttpPost;
import com.dfire.retail.app.manage.network.RequestParameter;
import com.dfire.retail.app.manage.network.RequestResultCallback;
import com.dfire.retail.app.manage.util.JsonUtil;
import com.dfire.retail.app.manage.util.ToastUtil;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

public class AdjustmentReasonActivity extends Activity implements OnClickListener,OnItemClickListener{

	private ListView reasonListview;
	
	private ImageButton save,cancel;
	
	private List<Map> types;
	
	private String typeVal,typeName;
	
	private ImageView imageView;
	
	private ImageButton minus;
	
	private StockAdjustVo adjustVo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_adjustment_reason_item);
		this.findView();
		this.getReason();
	}
	
	private void findView(){
		Bundle b = getIntent().getExtras();
		adjustVo=(StockAdjustVo)b.getSerializable("adjustVo");
		if (adjustVo!=null) {
			typeVal = adjustVo.getAdjustReasonId();
		}
		types = new ArrayList<Map>();
		save = (ImageButton)findViewById(R.id.save);
		cancel = (ImageButton)findViewById(R.id.cancel);
		reasonListview = (ListView)findViewById(R.id.reasonListview);
		reasonListview.setDivider(null);
		minus = (ImageButton) findViewById(R.id.minus);
		minus.setOnClickListener(this);
		save.setOnClickListener(this);
		cancel.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		if (v==cancel) {
			finish();
		}else if (v==save) {
			Intent select = new Intent(AdjustmentReasonActivity.this,StockGoodInfoActivity.class);
			select.putExtra("typeVal", typeVal);
			select.putExtra("typeName", typeName);
			setResult(100, select);
			finish();
		}else if (v==minus) {
			Intent addIntent = new Intent(AdjustmentReasonActivity.this,AdjustmentReasonAddActivity.class);
			startActivity(addIntent);
		}
	}
	@Override
	protected void onRestart() {
		super.onRestart();
		getReason();
	}
	/**
	 * 得到默认选择的view
	 */
	public void getView(ImageView view){
		imageView = view;
	}
	/**
	 * 查询调整原因
	 */
	private void getReason(){
		RequestParameter params = new RequestParameter(true);
		params.setUrl(Constants.BASE_URL + "stockAdjust/getReason");

		new AsyncHttpPost(params, new RequestResultCallback() {
			@SuppressWarnings({ "unchecked", "rawtypes" })
			@Override
			public void onSuccess(String str) {
				JsonObject jo = new JsonParser().parse(str).getAsJsonObject();
				JsonElement jsonElement = jo.get("returnCode");
				String returnCode = null;
				if (jsonElement != null) {
					returnCode = jsonElement.getAsString();
				}
				if (returnCode == null ||!returnCode.equals("success")) {
					ToastUtil.showShortToast(AdjustmentReasonActivity.this, "获取失败");
					return;
				}
				JsonUtil ju = new JsonUtil(str);
				if (ju.isError(AdjustmentReasonActivity.this)) {
					return;
				}
				if (types.size()>0) {
					types.clear();
				}
				types = (List<Map>) ju.get("listMap", new TypeToken<List<Map>>(){}.getType());
				reasonListview.setAdapter(new AdjustmentReasonAdapter(AdjustmentReasonActivity.this, types,typeVal));
				reasonListview.setOnItemClickListener(AdjustmentReasonActivity.this);
			}

			@Override
			public void onFail(Exception e) {
				ToastUtil.showShortToast(AdjustmentReasonActivity.this, "获取失败");
				e.printStackTrace();
			}
		}).execute();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		ImageView image = (ImageView)view.findViewById(R.id.typeiv);
		if (imageView != null) {
			imageView.setVisibility(View.GONE);
			imageView = image;
			imageView.setVisibility(View.VISIBLE);
		}else {
			imageView = image;
			imageView.setVisibility(View.VISIBLE);
		}
		this.typeVal = (String) types.get(position).get("typeVal");
		this.typeName = (String) types.get(position).get("typeName");
	}
}
