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
import com.dfire.retail.app.manage.util.ToastUtil;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import android.app.ProgressDialog;
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

	/* (non-Javadoc)
	 * @see com.dihuo.twodfire.retail.activity.goodsmanager.GoodsManagerBaseActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_goods_sort_layout);
		setTitleText(Constants.GOODS_SORT);
		hideRight();
		setLeftBtn(R.drawable.shutdown, Constants.EMPTY_STRING);
		setBack();
		sorts = (ListView) findViewById(R.id.goods_sort_list);
		findViewById(R.id.add).setOnClickListener(this);
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
		final ProgressDialog pd = ProgressDialog.show(this, null, Constants.WAIT_GET_CATEGORY, true, false);
		RequestParameter params = new RequestParameter(true);
		params.setUrl(Constants.CATEGORY_LIST_URL);
		new AsyncHttpPost(params, new RequestResultCallback() {

			public void onSuccess(String str) {
				pd.dismiss();
				JsonObject jo = new JsonParser().parse(str).getAsJsonObject();
				JsonElement jsonElement = jo.get(Constants.RETURN_CODE);
				String returnCode = null;
				if (jsonElement != null) {
					returnCode = jsonElement.getAsString();
				}

				if (returnCode == null || !returnCode.equals(Constants.LSUCCESS)) {
					ToastUtil.showShortToast(GoodsSortListActivity.this, Constants.getErrorInf(null, null));
					return;
				}
				List<CategoryVo> categoryVo =  new Gson().fromJson(jo.get(Constants.CATEGORY_LIST), new TypeToken<List<CategoryVo>>(){}.getType());
				categorys = new ArrayList<Category>();
				getCategory(Constants.EMPTY_STRING, categoryVo,Constants.EMPTY_STRING,categorys, 0);
				List<String> categorysString = new ArrayList<String>();
				for (Category item : categorys) {
					categorysString.add(item.name);
				}
				sorts.setAdapter(new GoodsSortListAdapter(GoodsSortListActivity.this, categorys));
				sorts.setOnItemClickListener(GoodsSortListActivity.this);
			}	

			@Override
			public void onFail(Exception e) {
				pd.dismiss();
				ToastUtil.showShortToast(GoodsSortListActivity.this, Constants.getErrorInf(null, null));
				e.printStackTrace();
			}
		}).execute();
	}

	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.add:
			ArrayList<Category> parentCategorys = new ArrayList<Category>();
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
