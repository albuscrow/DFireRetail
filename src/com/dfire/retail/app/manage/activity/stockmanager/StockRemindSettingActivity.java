package com.dfire.retail.app.manage.activity.stockmanager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.RetailApplication;
import com.dfire.retail.app.manage.activity.TitleActivity;
import com.dfire.retail.app.manage.data.StockInfoVo;
import com.dfire.retail.app.manage.global.Constants;

/**
 * 库存管理-提醒设置
 * @author wangpeng
 *
 */
public class StockRemindSettingActivity extends TitleActivity implements OnClickListener {

	private TextView search;
	private EditText input;
	private ImageView scan;
	private ListView stock_remind_setting_lv;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stock_remind_setting);
		setTitleText("提醒设置");
		showBackbtn();
		findView();
	}
	
	public void findView(){
		search=(TextView)findViewById(R.id.search);
		search.setOnClickListener(this);
		input=(EditText)findViewById(R.id.input);
		scan=(ImageView)findViewById(R.id.scan);
		scan.setOnClickListener(this);
		stock_remind_setting_lv=(ListView)findViewById(R.id.stock_remind_setting_lv);
	}
	
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.search:
			new Task().execute();
			
			break;
		}
	}
	
	class Task extends AsyncTask<Void, Void, Void> {
		ProgressDialog progressDialog;
		String res;
		StockInfoVo stockInfo;
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = new ProgressDialog(StockRemindSettingActivity.this);
			progressDialog.setCancelable(false);
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.setMessage("加载中，请稍后。。。");
			progressDialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {

			System.out.println("sessionId----------"+RetailApplication.getLoginResult().getJsessionId());
			JSONObject json = new JSONObject();
			try {
				json.put("sessionId", RetailApplication.getLoginResult().getJsessionId());
				json.put("version", "1");
				json.put("appKey", "1");
				json.put("timestamp", System.currentTimeMillis());
				json.put("sign", "");
				
				json.put("shopId", "8888");
				json.put("currentPage", "1");
				json.put("barCode", "");
				json.put("spell", "");
				json.put("shortCode", "");
			} catch (JSONException e1) {
				e1.printStackTrace();  
			}

			try {
				HttpClient httpClient = new DefaultHttpClient();
				HttpPost post = new HttpPost(Constants.BASE_URL +"stockInfo/list?");
				StringEntity entity = new StringEntity(json.toString());  
				entity.setContentEncoding("UTF-8");  
				entity.setContentType("application/json");  
				post.setEntity(entity); 
				HttpResponse response = httpClient.execute(post);

				if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){  
					HttpEntity ent = response.getEntity();  
					res = EntityUtils.toString(ent);
					System.out.println("----------------------"+res);

				}  

			} catch (Exception e) {  
				throw new RuntimeException(e);  
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			progressDialog.dismiss();
			if (res != null) {
		
				try {
					parserJson(res);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				/////////////////////////////////////////适配
			}
			super.onPostExecute(result);
		}
		
		protected void parserJson(String result) throws JSONException {
			stockInfo=new StockInfoVo();
			

		}

	}

}