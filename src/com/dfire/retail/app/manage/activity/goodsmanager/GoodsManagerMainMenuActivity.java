package com.dfire.retail.app.manage.activity.goodsmanager;

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.adapter.GoodsManagerMenuAdapter;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;


/**
 * The Class GoodsManagerMainMenuActivity.
 * 
 * @author albuscrow
 */
public class GoodsManagerMainMenuActivity extends GoodsManagerBaseActivity implements OnItemClickListener{
	
	/** The Constant CHAIFEN. */
	public static final int CHAIFEN = 2;
	
	/** The Constant ZUZHUANG. */
	public static final int ZUZHUANG = 1;
	
	/** The Constant JIAGONG. */
	public static final int JIAGONG = 3;

	/* (non-Javadoc)
	 * @see com.dihuo.twodfire.retail.activity.goodsmanager.GoodsManagerBaseActivity#onCreate(android.os.Bundle)
	 */
	@Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_goods_manager_menu);
	        setTitleText("商品管理");
	        setBack();
	        ListView list = (ListView) findViewById(R.id.setting_main_list);
	        list.setAdapter(new GoodsManagerMenuAdapter(this));
	        list.setOnItemClickListener(this);
	        hideRight();
	    }

	    /* (non-Javadoc)
    	 * @see android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget.AdapterView, android.view.View, int, long)
    	 */
    	@Override
	    public void onItemClick(AdapterView<?> parent, View view, int position,
	            long id) {
	        Intent intent = new Intent();
	        Class<?> tag = null;
	        switch (position) {//因为含有一个headview
	        case 0:
	        	tag = GoodsSortListActivity.class;
	        	break;
	        case 1:
	        	tag = GoodsSearchActivity.class;
	        	break;
	        case 2:
	        	//TODO
	        	break;
	        case 3:
	        	tag = GoodsSearchForOptActivity.class;
	        	intent.putExtra("mode", CHAIFEN);
	        	break;
	        case 4:
	        	tag = GoodsSearchForOptActivity.class;
	        	intent.putExtra("mode", ZUZHUANG);
	        	break;
	        case 5:
	        	tag = GoodsSearchForOptActivity.class;
	        	intent.putExtra("mode", JIAGONG);
	        	break;
	     
	        }
	        
	        if (tag != null) {
	            intent.setClass(this, tag);
	            startActivity(intent);
	        }
	    }
}
