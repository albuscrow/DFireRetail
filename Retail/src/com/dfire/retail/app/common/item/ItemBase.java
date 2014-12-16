/************************* 版权声明 **********************************
 * 版权所有：Copyright (c) 2008, 2014 张向华
 *
 * 工程名称：	Retail
 * 创建者：	Administrator 创建日期： 2014年10月21日
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
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dfire.retail.app.common.item.listener.IItemIsChangeListener;
import com.dfire.retail.app.manage.util.StringUtils;

/**
 * 基础的表单项控件.
 * 
 * @author <a href="mailto:zxh1000@163.com">张向华</a>.
 * @version $Revision: 1570 $
 * @date 2014年10月21日 下午3:26:33
 */
public class ItemBase extends LinearLayout {
	
	public ItemBase(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * <code>当前值</code>.
	 */
	private String currVal;
	/**
	 * <code>原来值</code>.
	 */
	private String oldVal;

	/**
	 * <code>状态：是否改变</code>.
	 */
	protected Boolean changeStatus;

	/**
	 * <code>改变标签</code>.
	 */
	protected TextView saveTag;
	/**
	 * <code>清空按钮</code>
	 */
	protected ImageView img;
	/**
	 * 修改数据监听器，如果数据有发生改变，更新UI Title信息
	 */
	protected IItemIsChangeListener itemIsChangeListener;
	

	public Boolean isChange() {
		changeStatus = !StringUtils.isEquals(currVal, oldVal);
		saveTag.setVisibility(changeStatus ? View.VISIBLE : View.GONE);
		//如果有修改，把是否修改的结果信息反馈给UI界面
		if(itemIsChangeListener != null)
			itemIsChangeListener.onItemIsChangeListener(ItemBase.this);
		
		return changeStatus;
	}
	/**
	 * @return
	 */
	public Boolean isChangeEditText() {
		changeStatus = !StringUtils.isEquals(currVal, oldVal);
		saveTag.setVisibility(changeStatus ? View.VISIBLE : View.GONE);
		//如果有修改，把是否修改的结果信息反馈给UI界面
		if(itemIsChangeListener != null)
			itemIsChangeListener.onItemIsChangeListener(ItemBase.this);
		return changeStatus;
	}
	/**
	 * 得到当前值.
	 * @return 当前值.
	 */
	public String getCurrVal() {
		return currVal;
	}

	/**
	 * 设置当前值.
	 * @param currVal 当前值.
	 */
	public void setCurrVal(String currVal) {
		this.currVal = currVal;
	}

	/**
	 * 得到原来值.
	 * @return 原来值.
	 */
	public String getOldVal() {
		return oldVal;
	}

	/**
	 * 设置原来值.
	 * @param oldVal 原来值.
	 */
	public void setOldVal(String oldVal) {
		this.oldVal = oldVal;
	}

	/**
	 * 得到状态：是否改变.
	 * @return 状态：是否改变.
	 */
	public Boolean getChangeStatus() {
		return changeStatus;
	}

	/**
	 * 设置状态：是否改变.
	 * @param changeStatus 状态：是否改变.
	 */
	public void setChangeStatus(Boolean changeStatus) {
		this.changeStatus = changeStatus;
	}

	/**
	 * 设置状态：是否改变.
	 * @param changeStatus 状态：是否改变.
	 */
	public void setIsChangeListener(IItemIsChangeListener itemIsChangeListener ) {
		this.itemIsChangeListener = itemIsChangeListener;
	}



}
