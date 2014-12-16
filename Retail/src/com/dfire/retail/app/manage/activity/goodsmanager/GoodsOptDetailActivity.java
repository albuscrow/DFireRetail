package com.dfire.retail.app.manage.activity.goodsmanager;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
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

import com.dfire.retail.app.common.retail.view.AlertDialog;
import com.dfire.retail.app.common.retail.view.OKAlertDialog;
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
	private GoodsVo resultGoods;


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
			if (up.getChildCount() >= 10) {
				final OKAlertDialog alertDialog = new OKAlertDialog(this);
				if (optMode == GoodsManagerMainMenuActivity.ZUZHUANG) {
					alertDialog.setMessage(Constants.CAN_NOT_MORE_ASSEMBLE);
				}else if (optMode == GoodsManagerMainMenuActivity.JIAGONG) {
					alertDialog.setMessage(Constants.CAN_NOT_MORE_PROCESS);
				}
				alertDialog.setPositiveButton(Constants.I_KNOW, new OnClickListener() {

					@Override
					public void onClick(View v) {
						alertDialog.dismiss();
					}
				});
				return;
			}
			add(optMode,false);
			break;
			
		case R.id.down_add_above:
			upOrDown = DOWN;
			if (down.getChildCount() == 0 || mode == ADD) {
				add(optMode, true);
			}
			break;
			
		case R.id.down_add:
			upOrDown = DOWN;
			add(optMode, true);
			break;
			
		case R.id.delete:
			final AlertDialog ad = new AlertDialog(this);
			ad.setMessage(Constants.DELETEP_FOR_RULE);
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
			break;
		case R.id.minus:
			if (v.getParent().getParent().getParent() == down) {
				changeNum((EditText)v.getTag(), true, DOWN);
			}else{
				changeNum((EditText)v.getTag(), true, UP);
			}
			break;
		case R.id.plus:
			if (v.getParent().getParent().getParent() == down) {
				changeNum((EditText)v.getTag(), false, DOWN);
			}else{
				changeNum((EditText)v.getTag(), false, UP);
			}
			break;

		default:
			break;
		}
	}
	

	private void changeNum(final EditText tag, boolean b, int upOrDown) {
		int value = Integer.parseInt(tag.getText().toString());
		if (!b) {
			++value;
			tag.setText("" + value);
		}else{
			if (value > 0) {
				--value;
				if (value == 0) {
					if (upOrDown == DOWN && mode == EDIT) {
						showAlert(tag);
					}else{
						isDeleteGoods(tag);
					}
				}else{
					tag.setText("" + value);
				}
			}else{
				tag.setText("" + value);
			}
		}
		switchToEditMode();
	}
	
	
	private void showAlert(EditText view) {
		final OKAlertDialog alertDialog = new OKAlertDialog(this);
		if (optMode == GoodsManagerMainMenuActivity.CHAIFEN) {
			alertDialog.setMessage(Constants.CAN_NOT_DELETE_SPLIT);
		}else{
			alertDialog.setMessage(Constants.CAN_NOT_DELETE_PROCESS);
		}
		alertDialog.setPositiveButton(Constants.I_KNOW, new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				alertDialog.dismiss();
			}
		});
		view.setText("1");
	}


	private void isDeleteGoods(final EditText tag) {
		
		final AlertDialog ad = new AlertDialog(this);
		ad.setMessage(Constants.DELETEP);
		ad.setPositiveButton(Constants.YES, new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				View rl = (View) tag.getParent().getParent();
				LinearLayout parent = (LinearLayout) rl.getParent();
				parent.removeView(rl);
				if (parent.getChildCount() == 0) {
					if (parent == up) {
						upAdd.setVisibility(View.VISIBLE);
					}else{
						downAdd.setVisibility(View.VISIBLE);
						findViewById(R.id.down_split).setVisibility(View.VISIBLE);
					}
					findViewById(R.id.up_split).setVisibility(View.VISIBLE);
				}
				ad.dismiss();
			}
		});
		ad.setNegativeButton(Constants.NO, new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				tag.setText(Constants.ONE);
				ad.dismiss();
			}
		});

	}
	
//	private void isDeleteRule(final EditText tag) {
//		
//		final AlertDialog ad = new AlertDialog(this);
//		ad.setMessage(Constants.DELETE_P);
//		ad.setPositiveButton(Constants.YES, new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				delete();
//				ad.dismiss();
//			}
//		});
//		ad.setNegativeButton(Constants.NO, new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				ad.dismiss();
//			}
//		});
//
//	}	


	private void delete() {
		RequestParameter params = new RequestParameter(true);
		switch (optMode) {
		case GoodsManagerMainMenuActivity.CHAIFEN:
			params.setUrl(Constants.GOODS_SPLIT_DELETE_URL);
			params.setParam(Constants.OPT_OLD_GOODS_ID, goodsVo.getGoodsId());
			break;
		case GoodsManagerMainMenuActivity.ZUZHUANG:
			params.setUrl(Constants.GOODS_ASSEMBLE_DELETE_URL);
			params.setParam(Constants.OPT_GOODS_ID, goodsVo.getGoodsId());
			break;
		case GoodsManagerMainMenuActivity.JIAGONG:
			params.setUrl(Constants.GOODS_PROCESSING_DELETE_URL);
			params.setParam(Constants.OPT_GOODS_ID, goodsVo.getGoodsId());
			break;
		default:
			break;
		}
		
		new AsyncHttpPost(this, params, new RequestResultCallback() {
			
			@Override
			public void onSuccess(String str) {
				JsonUtil ju = new JsonUtil(str);
				if (ju.isError(GoodsOptDetailActivity.this)) {
					return;
				}
//				ToastUtil.showShortToast(GoodsOptDetailActivity.this, Constants.DELETE_SUCCESS);
				setResult(RESULT_OK, new Intent()
				.putExtra(Constants.OPT_TYPE, GoodsSearchForOptActivity.DELETE)
				.putExtra(Constants.GOODS_ID, goodsVo.getGoodsId()));
				finish();
			}
			
			@Override
			public void onFail(Exception e) {
				ToastUtil.showShortToast(GoodsOptDetailActivity.this, Constants.NO_NET);
				e.printStackTrace();
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
				rl.findViewById(R.id.num_layout).setVisibility(View.INVISIBLE);
				upAdd.setVisibility(View.GONE);
				findViewById(R.id.up_split).setVisibility(View.INVISIBLE);
				GoodsHandleByGoodsVo temp = new GoodsHandleByGoodsVo();
				temp.setBarCode(goods.getBarCode());
				temp.setGoodsId(goods.getGoodsId());
				temp.setGoodsName(goods.getGoodsName());
				temp.setOldGoodsNum(Constants.PAGE_SIZE_OFFSET);
				temp.setType(goods.getType().toString());
				resultGoods = goods;
				rl.setTag(temp);
			}else{
				GoodsHandleByGoodsVo goodsHandleByGoodsVo = new GoodsHandleByGoodsVo();
				goodsHandleByGoodsVo.setBarCode(goods.getBarCode());
				goodsHandleByGoodsVo.setGoodsId(goods.getGoodsId());
				goodsHandleByGoodsVo.setGoodsName(goods.getGoodsName());
				goodsHandleByGoodsVo.setOldGoodsNum(1);
				goodsHandleByGoodsVo.setType(goods.getType().toString());
				rl.setTag(goodsHandleByGoodsVo);
			}
			setNumLayout(rl, 1,UP);
			if (up.getChildCount() >= 10) {
				upAdd.setVisibility(View.GONE);
				findViewById(R.id.up_split).setVisibility(View.INVISIBLE);
			}
			up.addView(rl);
		}else{
			rl = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.item_opt, null);
			down.removeAllViews();
			down.addView(rl);
			if (optMode == GoodsManagerMainMenuActivity.ZUZHUANG) {
				rl.findViewById(R.id.num_layout).setVisibility(View.INVISIBLE);
			}
			if (optMode != GoodsManagerMainMenuActivity.CHAIFEN) {
				resultGoods = goods;
			}
			setNumLayout(rl, 1, DOWN);
			downAdd.setVisibility(View.GONE);
			findViewById(R.id.down_split).setVisibility(View.INVISIBLE);
			GoodsVo temp = new GoodsVo();
			temp.setGoodsId(goods.getGoodsId());
			temp.setGoodsName(goods.getGoodsName());
			temp.setBarCode(goods.getBarCode());
//			temp.setLastVer(goodsHandle.getLastVer());
			rl.setTag(temp);
		}
		((TextView)rl.findViewById(R.id.name)).setText(goods.getGoodsName());
		((TextView)rl.findViewById(R.id.barCode)).setText(goods.getBarCode());
//		hideSplit();
	}


	private void setNumLayout(RelativeLayout rl, Integer i, final int downOrUp) {
		ImageButton add = (ImageButton) rl.findViewById(R.id.minus);
		ImageButton sub = (ImageButton) rl.findViewById(R.id.plus);
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
					if (downOrUp == DOWN) {
						showAlert(num);
					}else{
						isDeleteGoods(num);
					}
				}
			}

		});
	}


	private void add(int optMode, boolean needAskToAdd) {
		ArrayList<String> ids = new ArrayList<String>();
		getIds(ids, up);
		getIds(ids, down);
		boolean isBigGoods= false;
		if ((upOrDown == UP && optMode == GoodsManagerMainMenuActivity.CHAIFEN)
				|| (upOrDown == DOWN && (optMode != GoodsManagerMainMenuActivity.CHAIFEN))) {
			isBigGoods = true;
		}
		startActivityForResult(new Intent(this, ChooseGoodsActivity.class)
		.putExtra(Constants.IDS, ids)
		.putExtra(Constants.IS_BIG, isBigGoods)
		.putExtra(Constants.NEED_ASK_TO_ADD, needAskToAdd)
		.putExtra(Constants.MODE, optMode), optMode);
	}


	private void getIds(List<String> ids, LinearLayout viewGroup) {
		int count = viewGroup.getChildCount();
		for (int i = 0; i < count; ++i) {
			Object o = viewGroup.getChildAt(i).getTag();
			if (o instanceof GoodsVo) {
				ids.add(((GoodsVo)o).getGoodsId());
			}else{
				ids.add(((GoodsHandleByGoodsVo)o).getGoodsId());
			}
		}
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
		
		new AsyncHttpPost(this, params, new RequestResultCallback() {
			
			@Override
			public void onSuccess(String str) {
				JsonUtil ju = new JsonUtil(str);
				if (ju.isError(GoodsOptDetailActivity.this)) {
					return;
				}
//				ToastUtil.showShortToast(GoodsOptDetailActivity.this, Constants.SAVE_RULE_SUCCESS);
				if (mode == ADD) {
					setResult(RESULT_OK, new Intent()
					.putExtra(Constants.OPT_TYPE, GoodsSearchForOptActivity.ADD)
					.putExtra(Constants.GOODS, resultGoods));
				}
				finish();
			}
			
			@Override
			public void onFail(Exception e) {
				ToastUtil.showShortToast(GoodsOptDetailActivity.this, Constants.NO_NET);
				e.printStackTrace();
			}
		}).execute();
	}
	
	private GoodsHandleVo genGoodsHandle() {
		if (up.getChildCount() == 0) {
			ToastUtil.showShortToast(this, Constants.INPUT + upTitle.getText().toString() + "!");
			return null;
		}
		if (down.getChildCount() == 0) {
			ToastUtil.showShortToast(this, Constants.INPUT + downTitle.getText().toString() + "!");
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
//				goodsHandle.setLastVer(goodsDown.getLastVer());;
				goodsHandle.setMemo(goodsDown.getMemo());
				goodsHandle.setNewGoodsBarCode(goodsDown.getBarCode());
				goodsHandle.setNewGoodsId(goodsDown.getGoodsId());
				goodsHandle.setNewGoodsName(goodsDown.getGoodsName());
				String petailPrice = goodsDown.getPetailPrice();
				if (petailPrice != null) {
					goodsHandle.setRetailPrice(Float.parseFloat(petailPrice));
				}
				Integer newGoodsNum = getNum(downGoodsView);
				if (newGoodsNum == null) {
					return null;
				}
				goodsHandle.setNewGoodsNum(newGoodsNum);
				
				RelativeLayout newGoodsView = (RelativeLayout) up.getChildAt(0);
				GoodsHandleByGoodsVo goodsUp = (GoodsHandleByGoodsVo) newGoodsView.getTag();
				goodsHandle.setCheckType(goodsUp.getType());
				List<GoodsHandleByGoodsVo> oldGoodsList = new ArrayList<GoodsHandleByGoodsVo>();
//				GoodsHandleByGoodsVo oldGoods = new GoodsHandleByGoodsVo();
//				oldGoods.setBarCode(goodsUp.getBarCode());
//				oldGoods.setGoodsId(goodsUp.getGoodsId());
//				oldGoods.setGoodsName(goodsUp.getGoodsName());
				Integer num = getNum(newGoodsView);
				if (num == null) {
					return null;
				}
				goodsUp.setOldGoodsNum(Constants.ONE_INT); 
				goodsUp.setBarCode(null);
				oldGoodsList.add(goodsUp);
				goodsHandle.setOldGoodsList(oldGoodsList);
				break;
			case GoodsManagerMainMenuActivity.ZUZHUANG:
				RelativeLayout downView = (RelativeLayout) down.getChildAt(0);
				goodsMain = (GoodsVo) downView.getTag();
				handleType = GoodsManagerMainMenuActivity.ZUZHUANG;
				goodsHandle.setHandleType(handleType);
//				goodsHandle.setLastVer(goodsMain.getLastVer());;
				goodsHandle.setMemo(goodsMain.getMemo());
				goodsHandle.setNewGoodsBarCode(goodsMain.getBarCode());
				goodsHandle.setNewGoodsId(goodsMain.getGoodsId());
				goodsHandle.setNewGoodsName(goodsMain.getGoodsName());
				goodsHandle.setCheckType(goodsMain.getType().toString());
				
				String petailPriceZuzhuang = goodsMain.getPetailPrice();
				if (petailPriceZuzhuang != null) {
					goodsHandle.setRetailPrice(Float.parseFloat(petailPriceZuzhuang));
				}
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
//				goodsHandle.setLastVer(goodsMain.getLastVer());;
				goodsHandle.setMemo(goodsMain.getMemo());
				goodsHandle.setNewGoodsBarCode(goodsMain.getBarCode());
				goodsHandle.setNewGoodsId(goodsMain.getGoodsId());
				goodsHandle.setNewGoodsName(goodsMain.getGoodsName());
				goodsHandle.setCheckType(goodsMain.getType().toString());
				String petailPriceJiagong = goodsMain.getPetailPrice();
				if (petailPriceJiagong != null) {
					goodsHandle.setRetailPrice(Float.parseFloat(petailPriceJiagong));
				}
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
//				goodsHandle.setLastVer(goodsDown.getLastVer());
				goodsHandle.setMemo(goodsDown.getMemo());
				goodsHandle.setNewGoodsBarCode(goodsDown.getBarCode());
				goodsHandle.setNewGoodsId(goodsDown.getGoodsId());
				goodsHandle.setNewGoodsName(goodsDown.getGoodsName());
				Integer newGoodsNum = getNum(downGoodsView);
				if (newGoodsNum == null) {
					return null;
				}
				goodsHandle.setNewGoodsNum(newGoodsNum);
				
				String petailPriceZuzhuang = goodsDown.getPetailPrice();
				if (petailPriceZuzhuang != null) {
					goodsHandle.setRetailPrice(Float.parseFloat(petailPriceZuzhuang));
				}
			
				
				RelativeLayout newGoodsView = (RelativeLayout) up.getChildAt(0);
				GoodsHandleByGoodsVo goodsUp = (GoodsHandleByGoodsVo) newGoodsView.getTag();
				goodsHandle.setCheckType(goodsUp.getType());
				List<GoodsHandleByGoodsVo> oldGoodsList = new ArrayList<GoodsHandleByGoodsVo>();
//				GoodsHandleByGoodsVo oldGoods = new GoodsHandleByGoodsVo();
//				oldGoods.setBarCode(goodsUp.getBarCode());
//				oldGoods.setGoodsId(goodsUp.getGoodsId());
//				oldGoods.setGoodsName(goodsUp.getGoodsName());
//				Integer num = getNum(newGoodsView);
//				if (num == null) {
//					return null;
//				}
				goodsUp.setOldGoodsNum(Constants.PAGE_SIZE_OFFSET);
				oldGoodsList.add(goodsUp);
				goodsHandle.setOldGoodsList(oldGoodsList);
//				goodsHandle.setLastVer(1l);
//				goodsHandle.setRetailPrice(4f);
//				goodsHandle.setMemo("");
				break;
			case GoodsManagerMainMenuActivity.ZUZHUANG:
				RelativeLayout downView = (RelativeLayout) down.getChildAt(0);
				goodsMain = (GoodsVo) downView.getTag();
				handleType = GoodsManagerMainMenuActivity.ZUZHUANG;
				goodsHandle.setHandleType(handleType);
//				goodsHandle.setLastVer(goodsMain.getLastVer());;
				goodsHandle.setMemo(goodsMain.getMemo());
				goodsHandle.setNewGoodsBarCode(goodsMain.getBarCode());
				goodsHandle.setNewGoodsId(goodsMain.getGoodsId());
				goodsHandle.setNewGoodsName(goodsMain.getGoodsName());
				goodsHandle.setCheckType(goodsMain.getType().toString());
				String petailPrice = goodsMain.getPetailPrice();
				if (petailPrice != null) {
					goodsHandle.setRetailPrice(Float.parseFloat(petailPrice));
				}
				
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
//				goodsHandle.setLastVer(goodsMain.getLastVer());;
				goodsHandle.setMemo(goodsMain.getMemo());
				goodsHandle.setNewGoodsBarCode(goodsMain.getBarCode());
				goodsHandle.setNewGoodsId(goodsMain.getGoodsId());
				goodsHandle.setNewGoodsName(goodsMain.getGoodsName());
				goodsHandle.setCheckType(goodsMain.getType().toString());
				String petailPricejiagong = goodsMain.getPetailPrice();
				if (petailPricejiagong != null) {
					goodsHandle.setRetailPrice(Float.parseFloat(petailPricejiagong));
				}
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
		if (view.getVisibility() == View.INVISIBLE || view.getVisibility() == View.GONE) {
			return Constants.ONE_INT;
		}
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
		
		Drawable temp = null;
		switch (optMode) {
		case GoodsManagerMainMenuActivity.CHAIFEN:
			upTitle.setText(Constants.BIG_GOODS);
			upAdd.setText(Constants.CHOOSE_BIG_GOODS);
			downTitle.setText(Constants.SMALL_GOODS);
			downAdd.setText(Constants.CHOOSE_SMALL_GOODS);
			upAddAbove.setImageResource(R.drawable.ico_find_w);
//			upAddAbove.setOnClickListener(null);
			temp = getResources().getDrawable(R.drawable.ico_find_r);
			temp.setBounds(0, 0, temp.getIntrinsicWidth(), temp.getIntrinsicHeight());
			upAdd.setCompoundDrawables(temp, null, null, null);
			if (mode == ADD) {
				setTitleText(Constants.TITLE_ADD);
			}else{
				setTitleText(Constants.TITLE_SPLIT_RULE);
				upAddAbove.setVisibility(View.GONE);
				downAddAbove.setVisibility(View.GONE);
			}
			break;
		case GoodsManagerMainMenuActivity.ZUZHUANG:
			upTitle.setText(Constants.SMALL_GOODS);
			upAdd.setText(Constants.CHOOSE_SMALL_GOODS);
			downTitle.setText(Constants.BIG_GOODS);
			downAdd.setText(Constants.CHOOSE_BIG_GOODS);
			upAddAbove.setImageResource(R.drawable.ico_add_w);
			temp = getResources().getDrawable(R.drawable.ico_add_r);
			temp.setBounds(0, 0, temp.getIntrinsicWidth(), temp.getIntrinsicHeight());
			upAdd.setCompoundDrawables(temp, null, null, null);
			if (mode == ADD) {
				setTitleText(Constants.TITLE_ADD);
			}else{
				setTitleText(Constants.TITLE_ASSEMBLE_RULE);
				downAddAbove.setVisibility(View.GONE);
			}
			break;
		case GoodsManagerMainMenuActivity.JIAGONG:
			upTitle.setText(Constants.BEFORE_GOODS);
			upAdd.setText(Constants.CHOOSE_BEFORE_GOODS);
			downTitle.setText(Constants.AFTER_GOODS);
			downAdd.setText(Constants.CHOOSE_AFTER_GOODS);
			upAddAbove.setImageResource(R.drawable.ico_add_w);
			temp = getResources().getDrawable(R.drawable.ico_add_r);
			temp.setBounds(0, 0, temp.getIntrinsicWidth(), temp.getIntrinsicHeight());
			upAdd.setCompoundDrawables(temp, null, null, null);
			if (mode == ADD) {
				setTitleText(Constants.TITLE_ADD);
			}else{
				setTitleText(Constants.TITLE_PROCESS_RULE);
				downAddAbove.setVisibility(View.GONE);
			}
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
			params.setParam(Constants.OPT_OLD_GOODS_ID, goodsVo.getGoodsId());
			break;
		case GoodsManagerMainMenuActivity.ZUZHUANG:
			params.setUrl(Constants.GOODS_ASSEMBLE_DETAIL_URL);
			params.setParam(Constants.OPT_GOODS_ID, goodsVo.getGoodsId());
			break;
		case GoodsManagerMainMenuActivity.JIAGONG:
			params.setUrl(Constants.GOODS_PROCESSING_DETAIL_URL);
			params.setParam(Constants.OPT_GOODS_ID, goodsVo.getGoodsId());
			break;
		default:
			break;
		}
		
		new AsyncHttpPost(this, params, new RequestResultCallback() {

			@Override
			public void onSuccess(String str) {
				JsonUtil ju = new JsonUtil(str);
				if (ju.isError(GoodsOptDetailActivity.this)) {
					return;
				}
				goodsHandle = (GoodsHandleVo) ju.get(Constants.GOODS_HANDLE, GoodsHandleVo.class);
				fillView();
			}

			@Override
			public void onFail(Exception e) {
				ToastUtil.showShortToast(GoodsOptDetailActivity.this, Constants.NO_NET);
				e.printStackTrace();
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
			setNumLayout(chaifenda, goodsHandle.getNewGoodsNum(),DOWN);
			temp = new GoodsVo();
			temp.setGoodsId(goodsHandle.getNewGoodsId());
			temp.setGoodsName(goodsHandle.getNewGoodsName());
			temp.setBarCode(goodsHandle.getNewGoodsBarCode());
//			temp.setLastVer(goodsHandle.getLastVer());
			chaifenda.setTag(temp);
			
			
			for (GoodsHandleByGoodsVo xiao : goodsHandle.getOldGoodsList()) {
				RelativeLayout chaifenxiao = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.item_opt, null);
				chaifenxiao.findViewById(R.id.num_layout).setVisibility(View.INVISIBLE);
				up.addView(chaifenxiao);
				((TextView)chaifenxiao.findViewById(R.id.name)).setText(xiao.getGoodsName());
				((TextView)chaifenxiao.findViewById(R.id.barCode)).setText(xiao.getBarCode());
				setNumLayout(chaifenxiao, xiao.getOldGoodsNum(), UP);
				chaifenxiao.setTag(xiao);
			}
			break;
		case GoodsManagerMainMenuActivity.ZUZHUANG:
			RelativeLayout zuzhuangda = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.item_opt, null);
			down.addView(zuzhuangda);
			zuzhuangda.findViewById(R.id.num_layout).setVisibility(View.INVISIBLE);
			((TextView)zuzhuangda.findViewById(R.id.name)).setText(goodsHandle.getNewGoodsName());
			((TextView)zuzhuangda.findViewById(R.id.barCode)).setText(goodsHandle.getNewGoodsBarCode());
			setNumLayout(zuzhuangda, goodsHandle.getNewGoodsNum(), DOWN);
			temp = new GoodsVo();
			temp.setGoodsId(goodsHandle.getNewGoodsId());
			temp.setGoodsName(goodsHandle.getNewGoodsName());
			temp.setBarCode(goodsHandle.getNewGoodsBarCode());
//			temp.setLastVer(goodsHandle.getLastVer());
			Float price =goodsHandle.getRetailPrice();
			if (price != null) {
				temp.setPetailPrice(String.valueOf(price));
			}
			zuzhuangda.setTag(temp);
			
			for (GoodsHandleByGoodsVo xiao : goodsHandle.getOldGoodsList()) {
				RelativeLayout zuzhuangxiao = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.item_opt, null);
				up.addView(zuzhuangxiao);
				((TextView)zuzhuangxiao.findViewById(R.id.name)).setText(xiao.getGoodsName());
				((TextView)zuzhuangxiao.findViewById(R.id.barCode)).setText(xiao.getBarCode());
				setNumLayout(zuzhuangxiao, xiao.getOldGoodsNum(),UP);
				zuzhuangxiao.setTag(xiao);
			}
			break;
		case GoodsManagerMainMenuActivity.JIAGONG:
			RelativeLayout jiagongda = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.item_opt, null);
			down.addView(jiagongda);
			((TextView)jiagongda.findViewById(R.id.name)).setText(goodsHandle.getNewGoodsName());
			((TextView)jiagongda.findViewById(R.id.barCode)).setText(goodsHandle.getNewGoodsBarCode());
			setNumLayout(jiagongda, goodsHandle.getNewGoodsNum(), DOWN);
			temp = new GoodsVo();
			temp.setGoodsId(goodsHandle.getNewGoodsId());
			temp.setGoodsName(goodsHandle.getNewGoodsName());
			temp.setBarCode(goodsHandle.getNewGoodsBarCode());
//			temp.setLastVer(goodsHandle.getLastVer());
			Float priceJiagong =goodsHandle.getRetailPrice();
			if (priceJiagong != null) {
				temp.setPetailPrice(String.valueOf(priceJiagong));
			}

			jiagongda.setTag(temp);
			

			for (GoodsHandleByGoodsVo xiao : goodsHandle.getOldGoodsList()) {
				RelativeLayout jiagongxiao = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.item_opt, null);
				up.addView(jiagongxiao);
				((TextView)jiagongxiao.findViewById(R.id.name)).setText(xiao.getGoodsName());
				((TextView)jiagongxiao.findViewById(R.id.barCode)).setText(xiao.getBarCode());
				setNumLayout(jiagongxiao, xiao.getOldGoodsNum(), UP);
				jiagongxiao.setTag(xiao);
			}
			
			break;
		default:
			break;
		}
		
		if (optMode == GoodsManagerMainMenuActivity.CHAIFEN) {
			upAdd.setVisibility(View.GONE);
			findViewById(R.id.up_split).setVisibility(View.INVISIBLE);
		}

		downAdd.setVisibility(View.GONE);
		findViewById(R.id.down_split).setVisibility(View.INVISIBLE);
		
//		hideSplit();
	}


//	private void hideSplit() {
//		int childCount = down.getChildCount();
//		if (childCount > 0) {
//			down.getChildAt(childCount - 1).findViewById(R.id.split).setVisibility(View.INVISIBLE);
//		}
//		if (childCount > 1) {
//			down.getChildAt(childCount - 2).findViewById(R.id.split).setVisibility(View.VISIBLE);
//		}
//		
//		childCount = up.getChildCount();
//		if (childCount > 0) {
//			up.getChildAt(up.getChildCount() - 1).findViewById(R.id.split).setVisibility(View.GONE);
//		}
//		if (childCount > 1) {
//			up.getChildAt(up.getChildCount() - 2).findViewById(R.id.split).setVisibility(View.INVISIBLE);
//		}
//	}


	/* (non-Javadoc)
	 * @see com.dihuo.twodfire.retail.activity.goodsmanager.GoodsManagerBaseActivity#switchToEditMode()
	 */
	@Override
	protected void switchToEditMode() {
		super.switchToEditMode();
		findViewById(R.id.title_right).setOnClickListener(this);
		findViewById(R.id.title_left).setOnClickListener(this);
	}

}
