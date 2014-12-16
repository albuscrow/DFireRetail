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


package com.dfire.retail.app.common.item.listener;

import com.dfire.retail.app.common.item.ItemEditList;
import com.dfire.retail.app.common.item.ItemEditText;
import com.dfire.retail.app.common.item.SwitchRowItemEditText;

/**
 * 文本框变化，监听.
 * @author <a href="mailto:zxh1000@163.com">张向华</a>.
 * @version $Revision: 1570 $
 * @date 2014年10月22日 上午10:48:04
 */
public interface IItemTextChangeListener {
	
	
	/**
	 * 下拉表单类型输入变动事件.
	 * 
	 * @param obj
	 */
	void onItemEditTextChange(ItemEditList obj);
	
	
	
	/**
	 * 编辑表单类型输入变动事件.
	 * 
	 * @param obj
	 */
	void onItemEditTextChange(ItemEditText obj);
	
	/**
	 * 编辑表单类型输入变动事件.
	 * 第二行输入框
	 * @param obj
	 */
	void onItemEditTextChange(SwitchRowItemEditText obj);
}
