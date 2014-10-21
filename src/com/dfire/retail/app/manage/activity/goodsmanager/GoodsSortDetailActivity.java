package com.dfire.retail.app.manage.activity.goodsmanager;


import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.data.CategoryVo;
import com.dfire.retail.app.manage.global.Constants;
import com.dfire.retail.app.manage.network.AsyncHttpPost;
import com.dfire.retail.app.manage.network.RequestParameter;
import com.dfire.retail.app.manage.network.RequestResultCallback;
import com.dfire.retail.app.manage.util.ToastUtil;
import com.dfire.retail.app.manage.widget.spinner.SpinerPopWindow;
import com.dfire.retail.app.manage.widget.spinner.AbstractSpinerAdapter.IOnItemSelectListener;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;

/**
 * The Class GoodsSortDetailActivity.
 * 
 * @author albuscrow
 */
public class GoodsSortDetailActivity extends GoodsManagerBaseActivity implements
		OnClickListener, IOnItemSelectListener, OnCheckedChangeListener{
	

	/** The category. */
	private Category category;
	
	/** The spinner. */
	private SpinerPopWindow spinner;
	
	/** The parent category. */
	private List<Category> parentCategory;
	
	/** The parent id. */
	private String parentId = Constants.EMPTY_STRING;
	
	/** The parent id backup. */
	private String parentIdBackup = Constants.EMPTY_STRING;
	
	/** The has parent view. */
	private CheckBox hasParentView;
	
	/** The parent text view. */
	private TextView parentTextView;
	
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
		mode = getIntent().getIntExtra("mode", -1);
		if (mode == EDIT) {
			category = (Category) getIntent().getSerializableExtra("category");
			setEditTextContent(R.id.name, Constants.CATEGORY_NAME, category.name, Constants.EMPTY_STRING);
			setEditTextContent(R.id.code, Constants.CATEGORY_CODE, category.original.getCode(), Constants.EMPTY_STRING);
			boolean hasParent = category.original.hasParent();
			findViewById(R.id.shangji).setVisibility(View.GONE);
			findViewById(R.id.hasUp).setVisibility(View.GONE);
			setTitleText(category.name);
			findViewById(R.id.delete).setOnClickListener(this);
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
			setEditTextContent(R.id.name, Constants.CATEGORY_NAME, Constants.EMPTY_STRING, Constants.NECESSARY);
			setEditTextContent(R.id.code, Constants.CATEGORY_CODE, Constants.EMPTY_STRING, Constants.NOT_NECESSARY);
			hasParentView = setCheckBoxContent(R.id.hasUp, "此分类有上级", true);
			setTitleText("添加分类");
			hasParentView.setOnCheckedChangeListener(this);
			parentTextView = setSpinerConetent(R.id.shangji, "上级分类", category.parentName);
			parentTextView.setWidth((int) (parentTextView.getPaint().measureText(longest) * 1.4f));
			parentTextView.setOnClickListener(this);
//			parentId = category.original.getParentId();
//			parentIdBackup = parentId;
			spinner = new SpinerPopWindow(this);
			List<String> parentCategoryStr = new ArrayList<String>();
			for (Category item : parentCategory) {
				parentCategoryStr.add(item.name);
			}
			spinner.refreshData(parentCategoryStr, 0);
			spinner.setItemListener(this);
			findViewById(R.id.delete).setVisibility(View.GONE);;
		}
		
		hideRight();
		
		findViewById(R.id.title_left).setOnClickListener(this);
		findViewById(R.id.title_right).setOnClickListener(this);

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
		case R.id.secend:
			spinner.setWidth(parentTextView.getWidth());
			spinner.showAsDropDown(parentTextView);
			break;
		case R.id.delete:
			delete();
			break;
		default:
			break;
		}
	}
	
	
	/**
	 * Delete.
	 */
	private void delete() {
		final ProgressDialog pd = ProgressDialog.show(this, null, Constants.WAIT_DELETE_CATEGORY, true, false);
		RequestParameter params = new RequestParameter(true);
		params.setParam(Constants.CATEGORY_ID, category.id);
		params.setUrl(Constants.CATEGORY_DELETE_URL);
		new AsyncHttpPost(params, new RequestResultCallback() {
			
			@Override
			public void onSuccess(String str) {
				pd.dismiss();
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
				pd.dismiss();
				ToastUtil.showShortToast(GoodsSortDetailActivity.this, Constants.getErrorInf(null, null));
				e.printStackTrace();
			}
		}).execute();
	}

	/**
	 * Save.
	 */
	private void save() {
		final ProgressDialog pd = ProgressDialog.show(this, null, Constants.WAIT_SAVE_CATEGORY, true, false);
		category.original.setName(((TextView)findViewById(R.id.name).findViewById(R.id.secend)).getText().toString());
		category.original.setCode(((TextView)findViewById(R.id.code).findViewById(R.id.secend)).getText().toString());
		RequestParameter params = new RequestParameter(true);
		if (mode == EDIT) {
			params.setParam(Constants.OPT_TYPE,Constants.EDIT);
		}else{
			params.setParam(Constants.OPT_TYPE,Constants.ADD);
			category.original.setParentId(parentId);
		}
		try {
			params.setParam("category",new JSONObject(category.original.toString()));
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
	
		params.setUrl(Constants.CATEGORY_SAVE_URL);
		
		new	AsyncHttpPost(params, new RequestResultCallback() {
			
			@Override
			public void onSuccess(String str) {
				pd.dismiss();
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
					ToastUtil.showShortToast(GoodsSortDetailActivity.this, Constants.getErrorInf(exceptionCodeValue, null));			
					return;
				}
				if (mode == EDIT) {
					ToastUtil.showShortToast(GoodsSortDetailActivity.this, Constants.EDIT_SUCCESS);
				}else{
					ToastUtil.showShortToast(GoodsSortDetailActivity.this, Constants.ADD_SUCCESS);
				}
				finish();
			}
			@Override
			public void onFail(Exception e) {
				pd.dismiss();
				if (mode == EDIT) {
					ToastUtil.showShortToast(GoodsSortDetailActivity.this, Constants.EDIT_FAIL);
				}else{
					ToastUtil.showShortToast(GoodsSortDetailActivity.this, Constants.ADD_FAIL);
				}
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
	private CheckBox setCheckBoxContent(int id, String label, boolean b) {
		View layout = findViewById(id);
		TextView labelText = (TextView) layout.findViewById(R.id.check_title);
		labelText.setText(label);
		CheckBox checkBox = (CheckBox)findViewById(R.id.checkbox);
		checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				switchToEditMode();
			}
		});
		checkBox.setChecked(b);
		return checkBox;
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
	private TextView setSpinerConetent(int tongbu, String label, String value) {
		View layout = findViewById(tongbu);
		TextView tongbuLable = (TextView) layout.findViewById(R.id.main);
		tongbuLable.setText(label);
		TextView tongbuValue = (TextView) layout.findViewById(R.id.secend);
		tongbuValue.setText(value);
		return tongbuValue;
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
	 */
	private void setEditTextContent(int id, String lable, String content,
			String hint) {
		View layout = findViewById(id);
		TextView lableView = (TextView) layout.findViewById(R.id.main);
		if (lable != null) {
			lableView.setText(lable);
		}
		EditText inputView = (EditText) layout.findViewById(R.id.secend);
		inputView.setHint(hint);
		if (content != null) {
			inputView.setText(content);
		}
		inputView.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				switchToEditMode();
			}
		});
	}

	/* (non-Javadoc)
	 * @see com.dihuo.twodfire.retail.widget.spinner.AbstractSpinerAdapter.IOnItemSelectListener#onItemClick(int)
	 */
	@Override
	public void onItemClick(int pos) {
		parentId = parentCategory.get(pos).original.getId();
		parentTextView.setText(parentCategory.get(pos).name);
		switchToEditMode();
	}

	/* (non-Javadoc)
	 * @see android.widget.CompoundButton.OnCheckedChangeListener#onCheckedChanged(android.widget.CompoundButton, boolean)
	 */
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (isChecked) {
			findViewById(R.id.shangji).setVisibility(View.VISIBLE);
			parentId = parentIdBackup;
		}else{
			findViewById(R.id.shangji).setVisibility(View.GONE);
			parentIdBackup = parentId;
			parentId = Constants.EMPTY_STRING;
		}
	}

}
