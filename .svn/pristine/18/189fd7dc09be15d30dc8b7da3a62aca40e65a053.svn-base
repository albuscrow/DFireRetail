/************************* 版权声明 **********************************
 * 版权所有：Copyright (c) 2008, 2014 张向华
 *
 * 工程名称：	Retail
 * 创建者：	Administrator 创建日期： 2014年10月22日
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
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.util.StringUtils;

/**
 * 显示静态的文本项.
 * @author <a href="mailto:zxh1000@163.com">张向华</a>.
 * @version $Revision: 1570 $
 * @date 2014年10月22日 下午1:31:30
 */
public class ItemTextView extends LinearLayout {
	private TextView mlblName;
	private EditText lblVal;
	private TextView lblHit;
	
	/**
	 * <code>当前值</code>.
	 */
	private String currVal;
	public ItemTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.globe_item_text_view, this);
		initMainView();
	}
	/**
	 * 初始化控件.
	 * 
	 * @param label
	 *            标签名称.
	 * @param hit
	 *            提示信息.
	 */
	public void initLabel(String label, String hit) {
		mlblName.setText(StringUtils.isEmpty(label) ? "" : label);
		lblHit.setHint(StringUtils.isEmpty(hit) ? "" : hit);
		lblHit.setVisibility(StringUtils.isEmpty(hit) ? View.GONE
				: View.VISIBLE);
	}


	/**
	 * 初始化数值.
	 * @param dataLabel 数值标签.
	 * @param val 数值.
	 */
	public void initData(String dataLabel, String val) {
		this.currVal=StringUtils.isEmpty(val) ? "" : val;
		lblVal.setText(StringUtils.isEmpty(dataLabel) ? "" : dataLabel);
	}

	/**
	 * 得到当前值.
	 * @return
	 */
	public String getStrVal() {
		return getCurrVal();
	}

	
	/**
	 * 初始化.
	 */
	private void initMainView() {
		mlblName = (TextView) this.findViewById(R.id.mlblName);
		lblVal = (EditText) this.findViewById(R.id.lblVal);
		lblHit = (TextView) this.findViewById(R.id.lblHit);
	}

	/**
	 * 得到名称标签.
	 * 
	 * @return 名称标签.
	 */
	public TextView getLblName() {
		return mlblName;
	}

	/**
	 * 得到值处理.
	 * 
	 * @return 值处理.
	 */
	public EditText getLblVal() {
		return lblVal;
	}
	/**
	 * 得到当前值.
	 * @return 当前值.
	 */
	public String getCurrVal() {
		return currVal;
	}

}
