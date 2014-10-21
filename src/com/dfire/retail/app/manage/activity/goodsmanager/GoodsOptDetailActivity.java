package com.dfire.retail.app.manage.activity.goodsmanager;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.data.GoodsHandleByGoodsVo;
import com.dfire.retail.app.manage.data.GoodsHandleVo;
import com.dfire.retail.app.manage.data.GoodsVo;
import com.dfire.retail.app.manage.global.Constants;
import com.dfire.retail.app.manage.network.AsyncHttpPost;
import com.dfire.retail.app.manage.network.RequestParameter;
import com.dfire.retail.app.manage.network.RequestResultCallback;
import com.dfire.retail.app.manage.util.JsonUtil;
import com.dfire.retail.app.manage.util.ToastUtil;
import com.google.gson.GsonBuilder;


public class GoodsOptDetailActivity extends GoodsManagerBaseActivity implements OnClickListener {
	
	static public final int ADD = 1;
	static public final int EDIT = 2;
	private static final int UP = 0;
	private static final int DOWN = 1;
	private int mode;
	private boolean edited;
	private TextView upTitle;
	private LinearLayout up;
	private TextView upAdd;
	private TextView downTitle;
	private LinearLayout down;
	private TextView downAdd;
	private int optMode;
	private int upOrDown;
	private GoodsVo goodsVo;
	private GoodsHandleVo goodsHandle;
	private ImageButton upAddAbove;
	private ImageButton downAddAbove;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_left:
			finish();
			break;
			
		case R.id.title_right:
			save();
			break;
			
		case R.id.up_add_above:
		case R.id.up_add:
			upOrDown = UP;
			add(optMode);
			break;
			
		case R.id.down_add_above:
		case R.id.down_add:
			upOrDown = DOWN;
			add(optMode);
			break;
			
		case R.id.delete:
			delete();
			break;
		case R.id.add:
			changeNum((EditText)v.getTag(), true);
			break;
		case R.id.sub:
			changeNum((EditText)v.getTag(), false);
			break;

		default:
			break;
		}
	}
	

	private void changeNum(final EditText tag, boolean b) {
		int value = Integer.parseInt(tag.getText().toString());
		if (!b) {
			++value;
			tag.setText("" + value);
		}else{
			if (value > 0) {
				--value;
				if (value == 0) {
					isDeleteGoods(tag);
				}else{
					tag.setText("" + value);
				}
			}else{
				tag.setText("" + value);
			}
		}
		switchToEditMode();
	}


	private void isDeleteGoods(final EditText tag) {
		new AlertDialog.Builder(this).setTitle(Constants.DELETEP).setPositiveButton(Constants.YES, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				View rl = (View) tag.getParent().getParent();
				LinearLayout parent = (LinearLayout) rl.getParent();
				parent.removeView(rl);
			}
		
		}).setNegativeButton(Constants.NO, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				tag.setText(Constants.ONE);
			}
		}).show();
	}


	private void delete() {
		RequestParameter params = new RequestParameter(true);
		switch (optMode) {
		case GoodsManagerMainMenuActivity.CHAIFEN:
			params.setUrl(Constants.GOODS_SPLIT_DELETE_URL);
			break;
		case GoodsManagerMainMenuActivity.ZUZHUANG:
			params.setUrl(Constants.GOODS_ASSEMBLE_DELETE_URL);
			break;
		case GoodsManagerMainMenuActivity.JIAGONG:
			params.setUrl(Constants.GOODS_PROCESSING_DELETE_URL);
			break;
		default:
			break;
		}
		params.setParam(Constants.OPT_GOODS_ID, goodsVo.getGoodsId());
		
		final ProgressDialog pd = ProgressDialog.show(this, null, Constants.WAIT_DELETE_OPT);
		new AsyncHttpPost(params, new RequestResultCallback() {
			
			@Override
			public void onSuccess(String str) {
				pd.dismiss();
				JsonUtil ju = new JsonUtil(str);
				if (ju.isError(GoodsOptDetailActivity.this)) {
					return;
				}
				ToastUtil.showShortToast(GoodsOptDetailActivity.this, Constants.DELETE_SUCCESS);
				finish();
			}
			
			@Override
			public void onFail(Exception e) {
				pd.dismiss();
				ToastUtil.showUnknowError(GoodsOptDetailActivity.this);
			}
			
		}).execute();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != Activity.RESULT_OK) {
			return;
		}
		switchToEditMode();
		GoodsVo goods = (GoodsVo) data.getSerializableExtra(Constants.GOODS);
		RelativeLayout rl = null;
		if (upOrDown == UP) {
			rl = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.item_opt, null);
			if (optMode == GoodsManagerMainMenuActivity.CHAIFEN) {
				up.removeAllViews();
				rl.findViewById(R.id.num_layout).setVisibility(View.GONE);
				upAdd.setVisibility(View.GONE);
				GoodsHandleByGoodsVo temp = new GoodsHandleByGoodsVo();
				temp.setBarCode(goods.getBarCode());
				temp.setGoodsId(goods.getGoodsId());
				temp.setGoodsName(goods.getGoodsName());
				temp.setOldGoodsNum(Constants.PAGE_SIZE_OFFSET);
				rl.setTag(temp);
			}else{
				GoodsHandleByGoodsVo goodsHandleByGoodsVo = new GoodsHandleByGoodsVo();
				goodsHandleByGoodsVo.setBarCode(goods.getBarCode());
				goodsHandleByGoodsVo.setGoodsId(goods.getGoodsId());
				goodsHandleByGoodsVo.setGoodsName(goods.getGoodsName());
				goodsHandleByGoodsVo.setOldGoodsNum(1);
				rl.setTag(goodsHandleByGoodsVo);
			}
			setNumLayout(rl, 1);
			if (up.getChildCount() >= 10) {
				upAdd.setVisibility(View.GONE);
			}
			up.addView(rl);
		}else{
			rl = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.item_opt, null);
			down.removeAllViews();
			down.addView(rl);
			if (optMode == GoodsManagerMainMenuActivity.ZUZHUANG) {
				rl.findViewById(R.id.num_layout).setVisibility(View.GONE);
			}
			setNumLayout(rl, 1);
			downAdd.setVisibility(View.GONE);
			rl.setTag(goods);
		}
		((TextView)rl.findViewById(R.id.name)).setText(goods.getGoodsName());
		((TextView)rl.findViewById(R.id.barCode)).setText(goods.getBarCode());
	}


	private void setNumLayout(RelativeLayout rl, Integer i) {
		ImageButton add = (ImageButton) rl.findViewById(R.id.add);
		ImageButton sub = (ImageButton) rl.findViewById(R.id.sub);
		final EditText num = (EditText) rl.findViewById(R.id.num);
		num.setText("" + i);
		add.setTag(num);
		sub.setTag(num);
		add.setOnClickListener(this);
		sub.setOnClickListener(this);
		num.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				if (s.toString().equals(Constants.ZERO)) {
					isDeleteGoods(num);
				}
			}
		});
	}


	private void add(int optMode) {
		startActivityForResult(new Intent(this, ChooseGoodsActivity.class)
		.putExtra(Constants.MODE, optMode), optMode);
	}


	private void save() {
		GoodsHandleVo goodsHandleVo = genGoodsHandle();
		if (goodsHandleVo == null) {
			return;
		}
		RequestParameter params = new RequestParameter(true);
		switch (optMode) {
		case GoodsManagerMainMenuActivity.CHAIFEN:
			params.setUrl(Constants.GOODS_SPLIT_SAVE_URL);
			break;
		case GoodsManagerMainMenuActivity.ZUZHUANG:
			params.setUrl(Constants.GOODS_ASSEMBLE_SAVE_URL);
			break;
		case GoodsManagerMainMenuActivity.JIAGONG:
			params.setUrl(Constants.GOODS_PROCESSING_SAVE_URL);
			break;
		default:
			break;
		}
		if (mode == ADD) {
			params.setParam(Constants.OPT_TYPE, Constants.ADD);
		}else{
			params.setParam(Constants.OPT_TYPE, Constants.EDIT);
		}
		try {
			params.setParam(Constants.GOODS_HANDLE, new JSONObject(new GsonBuilder().serializeNulls().create().toJson(goodsHandleVo)));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		final ProgressDialog pd = ProgressDialog.show(this, null, Constants.WAIT_SAVE_RULE);
		new AsyncHttpPost(params, new RequestResultCallback() {
			
			@Override
			public void onSuccess(String str) {
				pd.dismiss();
				JsonUtil ju = new JsonUtil(str);
				if (ju.isError(GoodsOptDetailActivity.this)) {
					return;
				}
				ToastUtil.showShortToast(GoodsOptDetailActivity.this, Constants.SAVE_RULE_SUCCESS);
				finish();
			}
			
			@Override
			public void onFail(Exception e) {
				ToastUtil.showUnknowError(GoodsOptDetailActivity.this);
				pd.dismiss();
			}
		}).execute();
	}
	
	private GoodsHandleVo genGoodsHandle() {
		if (up.getChildCount() == 0) {
			ToastUtil.showShortToast(this, Constants.INPUT + upTitle.getText().toString());
			return null;
		}
		if (down.getChildCount() == 0) {
			ToastUtil.showShortToast(this, Constants.INPUT + downTitle.getText().toString());
			return null;
		}
		short handleType = 0;
		GoodsVo goodsMain = null;
		if (mode == ADD) {
			goodsHandle = new GoodsHandleVo();
			switch (optMode) {
			case GoodsManagerMainMenuActivity.CHAIFEN:
				handleType = GoodsManagerMainMenuActivity.CHAIFEN;
				GoodsVo goodsDown = null;
				RelativeLayout downGoodsView = (RelativeLayout) down.getChildAt(0);
				goodsDown = (GoodsVo) downGoodsView.getTag();
				goodsHandle.setHandleType(handleType);
				goodsHandle.setLastVer(goodsDown.getLastVer());;
				goodsHandle.setMemo(goodsDown.getMemo());
				goodsHandle.setNewGoodsBarCode(goodsDown.getBarCode());
				goodsHandle.setNewGoodsId(goodsDown.getGoodsId());
				goodsHandle.setNewGoodsName(goodsDown.getGoodsName());
				Integer newGoodsNum = getNum(downGoodsView);
				if (newGoodsNum == null) {
					return null;
				}
				goodsHandle.setNewGoodsNum(newGoodsNum);
				goodsHandle.setRetailPrice(Float.parseFloat(goodsDown.getPetailPrice()));
				
				RelativeLayout newGoodsView = (RelativeLayout) up.getChildAt(0);
				GoodsHandleByGoodsVo goodsUp = (GoodsHandleByGoodsVo) newGoodsView.getTag();
				List<GoodsHandleByGoodsVo> oldGoodsList = new ArrayList<GoodsHandleByGoodsVo>();
				GoodsHandleByGoodsVo oldGoods = new GoodsHandleByGoodsVo();
				oldGoods.setBarCode(goodsUp.getBarCode());
				oldGoods.setGoodsId(goodsUp.getGoodsId());
				oldGoods.setGoodsName(goodsUp.getGoodsName());
				Integer num = getNum(newGoodsView);
				if (num == null) {
					return null;
				}
				oldGoods.setOldGoodsNum(num); 
				oldGoodsList.add(oldGoods);
				goodsHandle.setOldGoodsList(oldGoodsList);
				break;
			case GoodsManagerMainMenuActivity.ZUZHUANG:
				RelativeLayout downView = (RelativeLayout) down.getChildAt(0);
				goodsMain = (GoodsVo) downView.getTag();
				handleType = GoodsManagerMainMenuActivity.ZUZHUANG;
				goodsHandle.setHandleType(handleType);
				goodsHandle.setLastVer(goodsMain.getLastVer());;
				goodsHandle.setMemo(goodsMain.getMemo());
				goodsHandle.setNewGoodsBarCode(goodsMain.getBarCode());
				goodsHandle.setNewGoodsId(goodsMain.getGoodsId());
				goodsHandle.setNewGoodsName(goodsMain.getGoodsName());
				goodsHandle.setRetailPrice(Float.parseFloat(goodsMain.getPetailPrice()));
				Integer numZuhuangDown = getNum(downView);
				goodsHandle.setNewGoodsNum(numZuhuangDown);
				
				List<GoodsHandleByGoodsVo> smallGoodsForZuzhuang = new ArrayList<GoodsHandleByGoodsVo>();
				for (int i = 0; i < up.getChildCount(); ++i) {
					RelativeLayout smallView = (RelativeLayout) up.getChildAt(i);
					GoodsHandleByGoodsVo temp = (GoodsHandleByGoodsVo) smallView.getTag();
					Integer numZuzhuang = getNum(smallView);
					if (numZuzhuang == null) {
						return null;
					}
					temp.setOldGoodsNum(numZuzhuang);
					smallGoodsForZuzhuang.add(temp);
				}			
				goodsHandle.setOldGoodsList(smallGoodsForZuzhuang);
				
				break;
			case GoodsManagerMainMenuActivity.JIAGONG:
				RelativeLayout downViewForJiagong = (RelativeLayout) down.getChildAt(0);
				goodsMain = (GoodsVo) downViewForJiagong.getTag();
				handleType = GoodsManagerMainMenuActivity.JIAGONG;
				goodsHandle.setHandleType(handleType);
				goodsHandle.setLastVer(goodsMain.getLastVer());;
				goodsHandle.setMemo(goodsMain.getMemo());
				goodsHandle.setNewGoodsBarCode(goodsMain.getBarCode());
				goodsHandle.setNewGoodsId(goodsMain.getGoodsId());
				goodsHandle.setNewGoodsName(goodsMain.getGoodsName());
				goodsHandle.setRetailPrice(Float.parseFloat(goodsMain.getPetailPrice()));
				Integer numJiagong = getNum(downViewForJiagong);
				if (numJiagong == null) {
					return null;
				}
				goodsHandle.setNewGoodsNum(numJiagong);
				
				
				List<GoodsHandleByGoodsVo> smallGoodsForJiagong = new ArrayList<GoodsHandleByGoodsVo>();
				for (int i = 0; i < up.getChildCount(); ++i) {
					RelativeLayout smallView = (RelativeLayout) up.getChildAt(i);
					GoodsHandleByGoodsVo temp = (GoodsHandleByGoodsVo) smallView.getTag();
					Integer numJiagongSmall = getNum(smallView);
					if (numJiagongSmall == null) {
						return null;
					}
					temp.setOldGoodsNum(numJiagongSmall);
					smallGoodsForJiagong.add(temp);
				}			
				goodsHandle.setOldGoodsList(smallGoodsForJiagong);
				break;
			default:
				break;
			}
		}else{
			switch (optMode) {
			case GoodsManagerMainMenuActivity.CHAIFEN:
				handleType = GoodsManagerMainMenuActivity.CHAIFEN;
				GoodsVo goodsDown = null;
				RelativeLayout downGoodsView = (RelativeLayout) down.getChildAt(0);
				goodsDown = (GoodsVo) downGoodsView.getTag();
				goodsHandle.setHandleType(handleType);
				goodsHandle.setLastVer(goodsDown.getLastVer());
				goodsHandle.setMemo(goodsDown.getMemo());
				goodsHandle.setNewGoodsBarCode(goodsDown.getBarCode());
				goodsHandle.setNewGoodsId(goodsDown.getGoodsId());
				goodsHandle.setNewGoodsName(goodsDown.getGoodsName());
				Integer newGoodsNum = getNum(downGoodsView);
				if (newGoodsNum == null) {
					return null;
				}
				goodsHandle.setNewGoodsNum(newGoodsNum);
//				goodsHandle.setRetailPrice(Float.parseFloat(goodsDown.getPetailPrice()));
				
				RelativeLayout newGoodsView = (RelativeLayout) up.getChildAt(0);
				GoodsHandleByGoodsVo goodsUp = (GoodsHandleByGoodsVo) newGoodsView.getTag();
				List<GoodsHandleByGoodsVo> oldGoodsList = new ArrayList<GoodsHandleByGoodsVo>();
				GoodsHandleByGoodsVo oldGoods = new GoodsHandleByGoodsVo();
				oldGoods.setBarCode(goodsUp.getBarCode());
				oldGoods.setGoodsId(goodsUp.getGoodsId());
				oldGoods.setGoodsName(goodsUp.getGoodsName());
//				Integer num = getNum(newGoodsView);
//				if (num == null) {
//					return null;
//				}
				oldGoods.setOldGoodsNum(1);
				oldGoodsList.add(oldGoods);
				goodsHandle.setOldGoodsList(oldGoodsList);
	
				break;
			case GoodsManagerMainMenuActivity.ZUZHUANG:
				RelativeLayout downView = (RelativeLayout) down.getChildAt(0);
				goodsMain = (GoodsVo) downView.getTag();
				handleType = GoodsManagerMainMenuActivity.ZUZHUANG;
				goodsHandle.setHandleType(handleType);
				goodsHandle.setLastVer(goodsMain.getLastVer());;
				goodsHandle.setMemo(goodsMain.getMemo());
				goodsHandle.setNewGoodsBarCode(goodsMain.getBarCode());
				goodsHandle.setNewGoodsId(goodsMain.getGoodsId());
				goodsHandle.setNewGoodsName(goodsMain.getGoodsName());
//				goodsHandle.setRetailPrice(Float.parseFloat(goodsMain.getPetailPrice()));
				Integer numZuhuangDown = getNum(downView);
				goodsHandle.setNewGoodsNum(numZuhuangDown);
				
				List<GoodsHandleByGoodsVo> smallGoodsForZuzhuang = new ArrayList<GoodsHandleByGoodsVo>();
				for (int i = 0; i < up.getChildCount(); ++i) {
					RelativeLayout smallView = (RelativeLayout) up.getChildAt(i);
					GoodsHandleByGoodsVo temp = (GoodsHandleByGoodsVo) smallView.getTag();
					Integer numZuzhuang = getNum(smallView);
					if (numZuzhuang == null) {
						return null;
					}
					temp.setOldGoodsNum(numZuzhuang);
					smallGoodsForZuzhuang.add(temp);
				}			
				goodsHandle.setOldGoodsList(smallGoodsForZuzhuang);
				break;
				
			case GoodsManagerMainMenuActivity.JIAGONG:
				RelativeLayout downViewForJiagong = (RelativeLayout) down.getChildAt(0);
				goodsMain = (GoodsVo) downViewForJiagong.getTag();
				handleType = GoodsManagerMainMenuActivity.JIAGONG;
				goodsHandle.setHandleType(handleType);
				goodsHandle.setLastVer(goodsMain.getLastVer());;
				goodsHandle.setMemo(goodsMain.getMemo());
				goodsHandle.setNewGoodsBarCode(goodsMain.getBarCode());
				goodsHandle.setNewGoodsId(goodsMain.getGoodsId());
				goodsHandle.setNewGoodsName(goodsMain.getGoodsName());
//				goodsHandle.setRetailPrice(Float.parseFloat(goodsMain.getPetailPrice()));
				Integer numJiagong = getNum(downViewForJiagong);
				if (numJiagong == null) {
					return null;
				}
				goodsHandle.setNewGoodsNum(numJiagong);
				
				
				List<GoodsHandleByGoodsVo> smallGoodsForJiagong = new ArrayList<GoodsHandleByGoodsVo>();
				for (int i = 0; i < up.getChildCount(); ++i) {
					RelativeLayout smallView = (RelativeLayout) up.getChildAt(i);
					GoodsHandleByGoodsVo temp = (GoodsHandleByGoodsVo) smallView.getTag();
					Integer numJiagongSmall = getNum(smallView);
					if (numJiagongSmall == null) {
						return null;
					}
					temp.setOldGoodsNum(numJiagongSmall);
					smallGoodsForJiagong.add(temp);
				}			
				goodsHandle.setOldGoodsList(smallGoodsForJiagong);
				break;
			default:
				break;
				
			}
		}
		return goodsHandle;
	}


	private Integer getNum(RelativeLayout view) {
		// TODO Auto-generated method stub
		try {
			Integer num = Integer.parseInt(((TextView)view.findViewById(R.id.num)).getText().toString());
			if (num <= 0) {
				ToastUtil.showShortToast(this, Constants.INPUT_RIGHT_NUM);
				return null;
			}
			return num;
		} catch (Exception e) {
			e.printStackTrace();
			ToastUtil.showShortToast(this, Constants.INPUT_RIGHT_NUM);
			return null;
		}
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_goods_opt_detail);
		goodsVo = (GoodsVo) getIntent().getSerializableExtra(Constants.GOODS);
		optMode = getIntent().getIntExtra(Constants.MODE, -1);
		if (goodsVo == null) {
			mode = ADD;
			findViewById(R.id.delete).setVisibility(View.GONE);
		}else{
			findViewById(R.id.delete).setOnClickListener(this);
			mode = EDIT;
			getDetail();
		}
		hideRight();
		setBack();
		
		upTitle = (TextView)findViewById(R.id.up_title);
		up = (LinearLayout)findViewById(R.id.up);
		upAdd = (TextView)findViewById(R.id.up_add);
		upAddAbove = (ImageButton)findViewById(R.id.up_add_above);
		upAdd.setOnClickListener(this);
		upAddAbove.setOnClickListener(this);
		
		downTitle = (TextView) findViewById(R.id.down_title);
		down = (LinearLayout)findViewById(R.id.down);
		downAdd = (TextView)findViewById(R.id.down_add);
		downAddAbove = (ImageButton)findViewById(R.id.down_add_above);
		downAddAbove.setOnClickListener(this);
		downAdd.setOnClickListener(this);
		
		switch (optMode) {
		case GoodsManagerMainMenuActivity.CHAIFEN:
			upTitle.setText(Constants.BIG_GOODS);
			upAdd.setText(Constants.CHOOSE_BIG_GOODS);
			downTitle.setText(Constants.SMALL_GOODS);
			downAdd.setText(Constants.CHOOSE_SMALL_GOODS);
			break;
		case GoodsManagerMainMenuActivity.ZUZHUANG:
			upTitle.setText(Constants.SMALL_GOODS);
			upAdd.setText(Constants.CHOOSE_SMALL_GOODS);
			downTitle.setText(Constants.BIG_GOODS);
			downAdd.setText(Constants.CHOOSE_BIG_GOODS);
			break;
		case GoodsManagerMainMenuActivity.JIAGONG:
			upTitle.setText(Constants.BEFORE_GOODS);
			upAdd.setText(Constants.CHOOSE_BEFORE_GOODS);
			downTitle.setText(Constants.AFTER_GOODS);
			downAdd.setText(Constants.CHOOSE_AFTER_GOODS);
			break;

		default:
			break;
		}
	}
	
	private void getDetail() {
		RequestParameter params = new RequestParameter(true);
		switch (optMode) {
		case GoodsManagerMainMenuActivity.CHAIFEN:
			params.setUrl(Constants.GOODS_SPLIT_DETAIL_URL);
			break;
		case GoodsManagerMainMenuActivity.ZUZHUANG:
			params.setUrl(Constants.GOODS_ASSEMBLE_DETAIL_URL);
			break;
		case GoodsManagerMainMenuActivity.JIAGONG:
			params.setUrl(Constants.GOODS_PROCESSING_DETAIL_URL);
			break;
		default:
			break;
		}
		final ProgressDialog pd = ProgressDialog.show(this, null, Constants.WAIT_OPT_DETAIL);
		params.setParam(Constants.OPT_GOODS_ID, goodsVo.getGoodsId());
		
		new AsyncHttpPost(params, new RequestResultCallback() {
			

			@Override
			public void onSuccess(String str) {
				pd.dismiss();
				JsonUtil ju = new JsonUtil(str);
				if (ju.isError(GoodsOptDetailActivity.this)) {
					return;
				}
				goodsHandle = (GoodsHandleVo) ju.get(Constants.GOODS_HANDLE, GoodsHandleVo.class);
				fillView();
			}

			@Override
			public void onFail(Exception e) {
				pd.dismiss();
				ToastUtil.showUnknowError(GoodsOptDetailActivity.this);
			}
		}).execute();
				
	}
	
	private void fillView() {
		GoodsVo temp = null;
		switch (optMode) {
		case GoodsManagerMainMenuActivity.CHAIFEN:
			
			RelativeLayout chaifenda = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.item_opt, null);
			down.addView(chaifenda);
			((TextView)chaifenda.findViewById(R.id.name)).setText(goodsHandle.getNewGoodsName());
			((TextView)chaifenda.findViewById(R.id.barCode)).setText(goodsHandle.getNewGoodsBarCode());
			setNumLayout(chaifenda, goodsHandle.getNewGoodsNum());
			temp = new GoodsVo();
			temp.setGoodsId(goodsHandle.getNewGoodsId());
			temp.setGoodsName(goodsHandle.getNewGoodsName());
			temp.setBarCode(goodsHandle.getNewGoodsBarCode());
			temp.setLastVer(goodsHandle.getLastVer());
			chaifenda.setTag(temp);
			
			
			for (GoodsHandleByGoodsVo xiao : goodsHandle.getOldGoodsList()) {
				RelativeLayout chaifenxiao = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.item_opt, null);
				chaifenxiao.findViewById(R.id.num_layout).setVisibility(View.GONE);
				up.addView(chaifenxiao);
				((TextView)chaifenxiao.findViewById(R.id.name)).setText(xiao.getGoodsName());
				((TextView)chaifenxiao.findViewById(R.id.barCode)).setText(xiao.getBarCode());
				setNumLayout(chaifenxiao, xiao.getOldGoodsNum());
				chaifenxiao.setTag(xiao);
			}
			break;
		case GoodsManagerMainMenuActivity.ZUZHUANG:
			RelativeLayout zuzhuangda = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.item_opt, null);
			down.addView(zuzhuangda);
			zuzhuangda.findViewById(R.id.num_layout).setVisibility(View.GONE);
			((TextView)zuzhuangda.findViewById(R.id.name)).setText(goodsHandle.getNewGoodsName());
			((TextView)zuzhuangda.findViewById(R.id.barCode)).setText(goodsHandle.getNewGoodsBarCode());
			setNumLayout(zuzhuangda, goodsHandle.getNewGoodsNum());
			temp = new GoodsVo();
			temp.setGoodsId(goodsHandle.getNewGoodsId());
			temp.setGoodsName(goodsHandle.getNewGoodsName());
			temp.setBarCode(goodsHandle.getNewGoodsBarCode());
			temp.setLastVer(goodsHandle.getLastVer());
			zuzhuangda.setTag(temp);
			
			for (GoodsHandleByGoodsVo xiao : goodsHandle.getOldGoodsList()) {
				RelativeLayout zuzhuangxiao = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.item_opt, null);
				up.addView(zuzhuangxiao);
				((TextView)zuzhuangxiao.findViewById(R.id.name)).setText(xiao.getGoodsName());
				((TextView)zuzhuangxiao.findViewById(R.id.barCode)).setText(xiao.getBarCode());
				setNumLayout(zuzhuangxiao, xiao.getOldGoodsNum());
				zuzhuangxiao.setTag(xiao);
			}
			break;
		case GoodsManagerMainMenuActivity.JIAGONG:
			RelativeLayout jiagongda = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.item_opt, null);
			down.addView(jiagongda);
			((TextView)jiagongda.findViewById(R.id.name)).setText(goodsHandle.getNewGoodsName());
			((TextView)jiagongda.findViewById(R.id.barCode)).setText(goodsHandle.getNewGoodsBarCode());
			setNumLayout(jiagongda, goodsHandle.getNewGoodsNum());
			temp = new GoodsVo();
			temp.setGoodsId(goodsHandle.getNewGoodsId());
			temp.setGoodsName(goodsHandle.getNewGoodsName());
			temp.setBarCode(goodsHandle.getNewGoodsBarCode());
			temp.setLastVer(goodsHandle.getLastVer());
			jiagongda.setTag(temp);
			

			for (GoodsHandleByGoodsVo xiao : goodsHandle.getOldGoodsList()) {
				RelativeLayout jiagongxiao = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.item_opt, null);
				up.addView(jiagongxiao);
				((TextView)jiagongxiao.findViewById(R.id.name)).setText(xiao.getGoodsName());
				((TextView)jiagongxiao.findViewById(R.id.barCode)).setText(xiao.getBarCode());
				setNumLayout(jiagongxiao, xiao.getOldGoodsNum());
				jiagongxiao.setTag(xiao);
			}
			
			break;
		default:
			break;
		}
		
		if (optMode == GoodsManagerMainMenuActivity.CHAIFEN) {
			upAdd.setVisibility(View.GONE);
		}

		downAdd.setVisibility(View.GONE);
	}


	/* (non-Javadoc)
	 * @see com.dihuo.twodfire.retail.activity.goodsmanager.GoodsManagerBaseActivity#switchToEditMode()
	 */
	@Override
	protected void switchToEditMode() {
		super.switchToEditMode();
		findViewById(R.id.title_right).setOnClickListener(this);
		findViewById(R.id.title_left).setOnClickListener(this);
		edited = true;
	}

}