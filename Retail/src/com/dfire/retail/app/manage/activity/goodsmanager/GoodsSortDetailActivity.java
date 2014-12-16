package com.dfire.retail.app.manage.activity.goodsmanager;


import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.dfire.retail.app.common.retail.view.AlertDialog;
import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.activity.goodsmanager.MyCheckBoxLayout.Listener;
import com.dfire.retail.app.manage.data.CategoryVo;
import com.dfire.retail.app.manage.global.Constants;
import com.dfire.retail.app.manage.network.AsyncHttpPost;
import com.dfire.retail.app.manage.network.RequestParameter;
import com.dfire.retail.app.manage.network.RequestResultCallback;
import com.dfire.retail.app.manage.util.ToastUtil;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.InputType;
import android.text.InputFilter.LengthFilter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

/**
 * The Class GoodsSortDetailActivity.
 * 
 * @author albuscrow
 */
public class GoodsSortDetailActivity extends GoodsManagerBaseActivity implements OnClickListener{
	

	/** The category. */
	private Category category;
	
	/** The spinner. */
	private OneColumnSpinner spinner;
	
	/** The parent category. */
	private List<Category> parentCategory;
	
	/** The parent id. */
	private String parentId = Constants.EMPTY_STRING;
	
	/** The parent id backup. */
	private String parentIdBackup = Constants.EMPTY_STRING;
	
	/** The has parent view. */
	private MyCheckBoxLayout hasParentView;
	
	/** The parent text view. */
	private MySpinnerLayout parentTextView;
	
	/** The Constant EDIT. */
	public static final int EDIT = 0;
	
	/** The Constant ADD. */
	public static final int ADD = 1;
	
	/** The mode. */
	private int mode;

	/* (non-Javadoc)
	 * @see com.dihuo.twodfire.retail.activity.goodsmanager.GoodsManagerBaseActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sort_detail);
		setWatcher(new CountWatcher(this));
		mode = getIntent().getIntExtra("mode", Constants.INVALID_INT);
		spinner = new OneColumnSpinner(this, (TextView) findViewById(R.id.shangji).findViewById(R.id.second));
		spinner.setTitleText(Constants.GOODS_SORT);
		if (mode == EDIT) {
			category = (Category) getIntent().getSerializableExtra(Constants.CATEGORY);
			setEditTextContent(R.id.name, Constants.CATEGORY_NAME, category.name, Constants.NECESSARY,
					InputType.TYPE_CLASS_TEXT, Constants.FIFTH_LENGTH);
			setEditTextContent(R.id.code, Constants.CATEGORY_CODE, category.original.getCode(),
					Constants.NOT_NECESSARY, InputType.TYPE_CLASS_NUMBER, Constants.EIGHT_LENGTH).setDigitsAndNum(true);
			boolean hasParent = category.original.hasParent();
			hasParentView = setCheckBoxContent(R.id.hasUp, Constants.HAS_PARENT, hasParent);
			hasParentView.setEditable(false);
			findViewById(R.id.shangji).setEnabled(false);
			TextView parentCategoryTextView = (TextView)findViewById(R.id.shangji).findViewById(R.id.second);
			parentCategoryTextView.setCompoundDrawables(null, null, null, null);
			parentCategoryTextView.setTextColor(getResources().getColor(R.color.not_necessary));
			
			if (hasParent) {
				parentTextView = setSpinerConetent(R.id.shangji, Constants.PARENT, category.parentName, spinner);
				parentTextView.setEditable(false);
			}else{
				findViewById(R.id.shangji).setVisibility(View.GONE);
			}
			
			setTitleText(category.name);
			findViewById(R.id.delete).setOnClickListener(this);
			if (category.original.getCategoryList() != null && category.original.getCategoryList().size() != 0) {
				findViewById(R.id.delete).setVisibility(View.GONE);
			}
			switchToBackMode();
		}else{
			category = new Category();
			category.original = new CategoryVo();
			parentCategory = (List<Category>) getIntent().getSerializableExtra("parentCategory");
			String longest = Constants.EMPTY_STRING;
			for (Category item : parentCategory) {
				if (longest.length() < item.name.length()) {
					longest = item.name;
				}
			}
			setEditTextContent(R.id.name, Constants.CATEGORY_NAME, Constants.EMPTY_STRING, Constants.NECESSARY,
					InputType.TYPE_CLASS_TEXT, Constants.FIFTH_LENGTH);
			setEditTextContent(R.id.code, Constants.CATEGORY_CODE, Constants.EMPTY_STRING,
					Constants.NOT_NECESSARY, InputType.TYPE_CLASS_NUMBER, Constants.EIGHT_LENGTH).setDigitsAndNum(true);
			hasParentView = setCheckBoxContent(R.id.hasUp, Constants.HAS_PARENT, false);
			setTitleText(Constants.TITLE_ADD);
			hasParentView.setListener(new Listener() {
				
				@Override
				public void checkedChange(boolean isChecked) {
					if (isChecked) {
						findViewById(R.id.shangji).setVisibility(View.VISIBLE);
						parentId = parentIdBackup;
					}else{
						findViewById(R.id.shangji).setVisibility(View.GONE);
						parentIdBackup = parentId;
						parentId = Constants.EMPTY_STRING;
					}
				}
			});
			parentTextView = setSpinerConetent(R.id.shangji, Constants.PARENT, category.parentName,spinner);
//			parentId = category.original.getParentId();
//			parentIdBackup = parentId;
			List<String> parentCategoryStr = new ArrayList<String>();
			for (Category item : parentCategory) {
				parentCategoryStr.add(item.name);
			}
			spinner.setData(parentCategoryStr);
		
			findViewById(R.id.delete).setVisibility(View.GONE);
			findViewById(R.id.shangji).setVisibility(View.GONE);
			
			switchToEditMode();
		}
		if (parentCategory == null || parentCategory.size() == 0) {
			if (parentTextView != null) {
				parentTextView.setEditable(false);
			}
			if (hasParentView != null) {
				hasParentView.setEditable(false);
			}
		}
//		setBack();
//		hideRight();
		
		findViewById(R.id.title_right).setOnClickListener(this);
		findViewById(R.id.title_left).setOnClickListener(this);

	}

	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_left:
			finish();
			break;

		case R.id.title_right:
			save();
			break;
			
		case R.id.delete:
			isDeleteCategory(genDeleteMessage());
			break;
		default:
			break;
		}
	}
	
	private String genDeleteMessage() {
//		category.goodsSum = 1;
		if (category.goodsSum == null || category.goodsSum == 0) {
			String name = category.name;
			if (name == null) {
				name = "";
			}
			return "确定删除[" + name + "]吗？";	
		}else{
			return Constants.DELETE_CATEGORY_P;
		}
		
	}

	@Override
	public void switchToBackMode() {
		if (mode == EDIT) {
			super.switchToBackMode();
		}
	}
	
	private void isDeleteCategory(String message) {
		
		final AlertDialog ad = new AlertDialog(this);
		ad.setMessage(message);
		ad.setPositiveButton(Constants.CONFIRM, new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				delete();
				ad.dismiss();
			}
		});
		ad.setNegativeButton(Constants.CANCEL, new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ad.dismiss();
			}
		});

		
	}	
	
	
	/**
	 * Delete.
	 */
	private void delete() {
		RequestParameter params = new RequestParameter(true);
		params.setParam(Constants.CATEGORY_ID, category.id);
		params.setUrl(Constants.CATEGORY_DELETE_URL);
		new AsyncHttpPost(this, params, new RequestResultCallback() {
			
			@Override
			public void onSuccess(String str) {
				JsonObject jo = new JsonParser().parse(str).getAsJsonObject();
				JsonElement jsonElement = jo.get(Constants.RETURN_CODE);
				String returnCode = null;
				if (jsonElement != null) {
					returnCode = jsonElement.getAsString();
				}
				if (returnCode == null || !returnCode.equals(Constants.LSUCCESS)) {
					JsonElement exceptionCode = jo.get(Constants.EXCEPTAION_CODE);
					String exceptionCodeValue = null;
					if (exceptionCode != null && !exceptionCode.isJsonNull()) {
						exceptionCodeValue = exceptionCode.getAsString();
					}
					ToastUtil.showShortToast(GoodsSortDetailActivity.this, Constants.getErrorInf(exceptionCodeValue, null));
					return;
				}
				finish();
			}
			
			@Override
			public void onFail(Exception e) {
				ToastUtil.showShortToast(GoodsSortDetailActivity.this, Constants.NO_NET);
				e.printStackTrace();
			}
		}).execute();
	}

	/**
	 * Save.
	 */
	private void save() {
		final TextView categoryName = (TextView)findViewById(R.id.name).findViewById(R.id.second);
		String name = categoryName.getText().toString();
		if (name.length() == 0) {
			ToastUtil.showShortToast(this, Constants.INPUT_CATEGORY_NAME);
			categoryName.requestFocus();
			return;
		}
		category.original.setName(name);
		
		final TextView categoryCode = (TextView)findViewById(R.id.code).findViewById(R.id.second);
		String code = categoryCode.getText().toString();
		if (code.length() == 0) {
			category.original.setCode(null);
		}else{
			category.original.setCode(code);
		}
		RequestParameter params = new RequestParameter(true);
		if (mode == EDIT) {
			params.setParam(Constants.OPT_TYPE,Constants.EDIT);
		}else{
			params.setParam(Constants.OPT_TYPE,Constants.ADD);
			if (parentId != null && parentId.length() == 0) {
				parentId = null;
			}
			category.original.setParentId(parentId);
		}
//		category.original.setCategoryList(null);
		try {
			params.setParam("category",new JSONObject(category.original.toString()));
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
	
		params.setUrl(Constants.CATEGORY_SAVE_URL);
		
		new	AsyncHttpPost(this, params, new RequestResultCallback() {
			
			@Override
			public void onSuccess(String str) {
				JsonObject jo = new JsonParser().parse(str).getAsJsonObject();
				JsonElement jsonElement = jo.get(Constants.RETURN_CODE);
				String returnCode = null;
				if (jsonElement != null) {
					returnCode = jsonElement.getAsString();
				}
				if (returnCode == null || !returnCode.equals(Constants.LSUCCESS)) {
					JsonElement exceptionCode = jo.get(Constants.EXCEPTAION_CODE);
					String exceptionCodeValue = null;
					if (exceptionCode != null) {
						exceptionCodeValue = exceptionCode.getAsString();
					}
					ToastUtil.showShortToast(GoodsSortDetailActivity.this, Constants.getErrorInf(exceptionCodeValue, Constants.NO_NET));			
					if (exceptionCodeValue.equals(Constants.GOODS_ERROR_13)) {
						categoryName.requestFocus();
					}else if(exceptionCodeValue.equals(Constants.GOODS_ERROR_25)){
						categoryCode.requestFocus();
					}
					return;
				}
//				if (mode == EDIT) {
//					ToastUtil.showShortToast(GoodsSortDetailActivity.this, Constants.EDIT_SUCCESS);
//				}else{
//					ToastUtil.showShortToast(GoodsSortDetailActivity.this, Constants.ADD_SUCCESS);
//				}
				finish();
			}
			@Override
			public void onFail(Exception e) {
				
				ToastUtil.showShortToast(GoodsSortDetailActivity.this, Constants.NO_NET);
//				if (mode == EDIT) {
//					ToastUtil.showShortToast(GoodsSortDetailActivity.this, Constants.EDIT_FAIL);
//				}else{
//					ToastUtil.showShortToast(GoodsSortDetailActivity.this, Constants.ADD_FAIL);
//				}
				e.printStackTrace();
			}
		}).execute();
	}

	/**
	 * Sets the check box content.
	 * 
	 * @param id
	 *            the id
	 * @param label
	 *            the label
	 * @param b
	 *            the b
	 * @return the check box
	 */
	private MyCheckBoxLayout setCheckBoxContent(int id, String label, boolean b) {
		MyCheckBoxLayout layout = (MyCheckBoxLayout) findViewById(id);
		layout.init(label, b);
		layout.clearSaveFlag();
		return layout;
	}

	/**
	 * Sets the spiner conetent.
	 * 
	 * @param tongbu
	 *            the tongbu
	 * @param label
	 *            the label
	 * @param value
	 *            the value
	 * @return the text view
	 */
	private MySpinnerLayout setSpinerConetent(int tongbu, String label, String value, OneColumnSpinner spinner) {
		MySpinnerLayout layout = (MySpinnerLayout) findViewById(tongbu);
		layout.init(label, value, spinner);
		layout.clearSaveFlag();
		layout.setListener(new com.dfire.retail.app.manage.activity.goodsmanager.MySpinnerLayout.Listener() {
			
			@Override
			public String confirm(int pos) {
				parentId = parentCategory.get(pos).original.getId();
				return parentCategory.get(pos).name;
			}
			
			@Override
			public void cancel() {
			}
		});
		return layout;
	}

	/**
	 * Sets the edit text content.
	 * 
	 * @param id
	 *            the id
	 * @param lable
	 *            the lable
	 * @param content
	 *            the content
	 * @param hint
	 *            the hint
	 * @param typeClassText 
	 */
	private MyEditTextLayout setEditTextContent(int id, String label, String content, String hint, int typeClassText, int Maxlength) {
		MyEditTextLayout layout = (MyEditTextLayout) findViewById(id);
		layout.init(label, content, hint, typeClassText, Maxlength);
		layout.clearSaveFlag();
		return layout;
	}

}
