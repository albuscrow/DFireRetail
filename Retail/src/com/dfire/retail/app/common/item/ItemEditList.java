/************************* 版权声明 **********************************
 * 版权所有：Copyright (c) 2008, 2014 张向华
 *
 * 工程名称：	Retail
 * 创建者：	zxh 创建日期： 2014年10月20日
 * 创建记录：	创建类结构。
 *
 * ************************* 变更记录 ********************************
 * 修改者： 
 * 修改日期：
 * 修改记录：
 *
 * 
 * ......************************* To Do List*****************************
 * 
 *
 * Suberversion 信息
 * ID:			$Id$
 * 源代码URL：	$HeadURL$
 * 最后修改者：	$LastChangedBy$
 * 最后修改日期：	$LastChangedDate$
 * 最新版本：		$LastChangedRevision$
 **/

package com.dfire.retail.app.common.item;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dfire.retail.app.common.item.listener.IItemListListener;
import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.util.StringUtils;

/**
 * 表单项-列表项.
 * 
 * @author <a href="mailto:zxh1000@163.com">张向华</a>.
 * @version $Revision: 1570 $
 * @date 2014年10月20日 下午4:07:30
 */
public class ItemEditList extends ItemBase {
	private TextView lblName;
	private ImageView img;
	private ImageView btn;
	private TextView lblVal;
	private TextView lblHit;
	private View itemlistline;

	/**
	 * <code>注册事件处理</code>.
	 */
	private IItemListListener itemClickListener;

	/**
	 * @param context
	 * @param attrs
	 */
	public ItemEditList(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.globe_item_edit_list, this);
		initMainView();
		saveTag.setVisibility(View.GONE);
	}

	/**
	 * 初始化控件.
	 * 
	 * @param label
	 *            标签名称.
	 * @param hit
	 *            提示信息.
	 * @param sampleItemClickListener
	 *            代理.
	 */
	public void initLabel(String label, String hit,
			IItemListListener itemClickListener) {
		initLabel(label, hit, Boolean.FALSE, itemClickListener);
	}

	/**
	 * @param label 标签名称.
	 * @param hit 提示信息.
	 * @param isRequest
	 * @param sampleItemClickListener 代理.
	 */
	public void initLabel(String label, String hit, Boolean isRequest,
			IItemListListener itemClickListener) {
		lblName.setText(StringUtils.isEmpty(label) ? "" : label);
		lblHit.setHint(StringUtils.isEmpty(hit) ? "" : hit);
		lblHit.setVisibility(StringUtils.isEmpty(hit) ? View.GONE
				: View.VISIBLE);
		lblVal.setHint(isRequest ? "必填" : "可不填");
		lblVal.setHintTextColor(isRequest ? Color.RED : Color.GRAY);
		this.itemClickListener = itemClickListener;
	}

	public void initData(String dataLabel, String val) {
		setOldVal(StringUtils.isEmpty(val) ? "" : val);
		changeData(dataLabel, val);
	}

	public void changeData(String dataLabel, String val) {
		setCurrVal(StringUtils.isEmpty(val) ? "" : val);
		lblVal.setText(StringUtils.isEmpty(dataLabel) ? "" : dataLabel);
		isChange();
	}

	public String getStrVal() {
		return getCurrVal();
	}

	public void clearChange() {
		setOldVal(getCurrVal());
		isChange();
	}

	/**
	 * 初始化.
	 */
	private void initMainView() {
		lblName = (TextView) this.findViewById(R.id.lblName);
		lblVal = (TextView) this.findViewById(R.id.lblVal);
		lblHit = (TextView) this.findViewById(R.id.lblHit);
		saveTag = (TextView) this.findViewById(R.id.saveTag);
		img = (ImageView) this.findViewById(R.id.img);
		btn = (ImageView) this.findViewById(R.id.btn);
		//分隔线
		itemlistline  = (View) this.findViewById(R.id.itemlistline);
		lblVal.setFocusable(false);
		lblName.setFocusable(false);
		img.setFocusable(false);
		
		this.setFocusable(true);
		this.setFocusableInTouchMode(true);
		
		btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				itemClickListener.onItemListClick(ItemEditList.this);

			}
		});
		
		/*lblVal.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				String val=lblVal.getText().toString();
				setCurrVal(StringUtils.isEmpty(val) ? "" : val);
				isChange();
				if(itemChange!=null){
					itemChange.onItemEditTextChange(ItemEditList.this);
				}
				
			}
		});*/
	}

	/**
	 * 得到名称标签.
	 * 
	 * @return 名称标签.
	 */
	public TextView getLblName() {
		return lblName;
	}

	/**
	 * 得到图标.
	 * 
	 * @return 图标.
	 */
	public ImageView getImg() {
		return img;
	}
	/**
	 * 得到整个布局 去掉点击事件
	 */
	public void setNotClickable(boolean isNo){
		btn.setClickable(isNo);
	}
	
	/**
	 * 得到值处理.
	 * 
	 * @return 值处理.
	 */
	public TextView getLblVal() {
		return lblVal;
	}
	/**
	 * 返回未保存的信息
	 */
	public TextView getSaveTag() {
		return saveTag;
	}

	/**
	 * 隐藏行底的分隔线
	 */
	public void hindViewLine(){
		itemlistline.setVisibility(View.INVISIBLE);
	}
}
