package com.dfire.retail.app.manage.activity.goodsmanager;

import java.util.ArrayList;
import java.util.List;

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.adapter.GoodsSortListAdapter;
import com.dfire.retail.app.manage.data.CategoryVo;
import com.dfire.retail.app.manage.global.Constants;
import com.dfire.retail.app.manage.network.AsyncHttpPost;
import com.dfire.retail.app.manage.network.RequestParameter;
import com.dfire.retail.app.manage.network.RequestResultCallback;
import com.dfire.retail.app.manage.util.JsonUtil;
import com.dfire.retail.app.manage.util.ToastUtil;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

/**
 * The Class GoodsSortListActivity.
 * 
 * @author albuscrow
 */
public class GoodsSortListActivity extends GoodsManagerBaseActivity implements
		OnItemClickListener, OnClickListener {
	
	/** The sorts. */
	private ListView sorts;
	
	/** The categorys. */
	private ArrayList<Category> categorys;

	private int index;

	private int top;

	/* (non-Javadoc)
	 * @see com.dihuo.twodfire.retail.activity.goodsmanager.GoodsManagerBaseActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_goods_sort_layout);
		setTitleText(Constants.GOODS_SORT);
		hideRight();
		setLeftBtn(R.drawable.noradius, Constants.SHUT_DOWN);
		setBack();
		sorts = (ListView) findViewById(R.id.goods_sort_list);
		addFooter(sorts);
		findViewById(R.id.minus).setOnClickListener(this);
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		getCategoryList();
		super.onResume();
	}

	/* (non-Javadoc)
	 * @see android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget.AdapterView, android.view.View, int, long)
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		
		index = parent.getFirstVisiblePosition();  
		View v = parent.getChildAt(0);  
		top = (v == null) ? 0 : v.getTop();
		ArrayList<Category> parentCategorys = new ArrayList<Category>();
		for (Category item : categorys) {
			if (item.depth < Constants.CATEGORY_DEPTH) {
				parentCategorys.add(item);
			}
		}
		startActivity(new Intent(this, GoodsSortDetailActivity.class)
		.putExtra(Constants.CATEGORY, categorys.get(position))
		.putExtra(Constants.MODE, GoodsSortDetailActivity.EDIT)
		.putExtra(Constants.PARENT_CATEGORY, parentCategorys));
	}
	
	/**
	 * Gets the category.
	 * 
	 * @param parentName
	 *            the parent name
	 * @param categoryVo
	 *            the category vo
	 * @param parent
	 *            the parent
	 * @param categorys
	 *            the categorys
	 * @param depth
	 *            the depth
	 * @return the category
	 */
	private void getCategory(String parentName, List<CategoryVo> categoryVo,
					String parent, List<Category> categorys, int depth) {
		for (CategoryVo item : categoryVo) {
			Category category = new Category();
			if (parent.length() != 0) {
				category.parents = parent.substring(0, parent.length() - 1);
				category.parentName = parentName;
			}
			category.name = item.getName();
			category.id = item.getId();
			category.depth = depth;
			category.original = item;
			category.goodsSum = item.getGoodsSum();
			categorys.add(category);
			if (item.getCategoryList() != null) {
				getCategory(item.getName(), item.getCategoryList(), parent + item.getName() + "-", categorys, depth + 1);
			}
		}
	}
	
	/**
	 * Gets the category list.
	 * 
	 * @return the category list
	 */
	private void getCategoryList() {
		RequestParameter params = new RequestParameter(true);
		params.setUrl(Constants.CATEGORY_LIST_URL);
		new AsyncHttpPost(this, params, new RequestResultCallback() {

			public void onSuccess(String str) {
				JsonUtil ju = new JsonUtil(str);
				if (ju.isError(GoodsSortListActivity.this)) {
					return;
				}
				
				List<CategoryVo> categoryVo =  (List<CategoryVo>) ju.get(Constants.CATEGORY_LIST, new TypeToken<List<CategoryVo>>(){}.getType());
				categorys = new ArrayList<Category>();
				if (categoryVo != null && categoryVo.size() != 0) {
					getCategory(Constants.EMPTY_STRING, categoryVo,Constants.EMPTY_STRING,categorys, 0);
					List<String> categorysString = new ArrayList<String>();
					for (Category item : categorys) {
						categorysString.add(item.name);
					}
				}
				sorts.setAdapter(new GoodsSortListAdapter(GoodsSortListActivity.this, categorys));
				sorts.setOnItemClickListener(GoodsSortListActivity.this);
				sorts.setSelectionFromTop(index, top);
			}	

			@Override
			public void onFail(Exception e) {
				ToastUtil.showShortToast(GoodsSortListActivity.this, Constants.NO_NET);
				e.printStackTrace();
			}
		},false).execute();
	}

	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.minus:
			ArrayList<Category> parentCategorys = new ArrayList<Category>();
			if (categorys == null) {
				categorys = new ArrayList<Category>();
			}
			for (Category item : categorys) {
				if (item.depth < Constants.CATEGORY_DEPTH) {
					parentCategorys.add(item);
				}
			}
			startActivity(new Intent(this, GoodsSortDetailActivity.class)
			.putExtra(Constants.PARENT_CATEGORY, parentCategorys)
			.putExtra(Constants.MODE, GoodsSortDetailActivity.ADD));
			break;

		default:
			break;
		}
	}

}
