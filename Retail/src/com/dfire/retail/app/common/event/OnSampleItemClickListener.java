/************************* 版权声明 **********************************
 * 版权所有：Copyright (c) 2008, 2013 张向华
 *
 * 工程名称：	rest
 * 创建者：	zxh 创建日期： 2013-7-24
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


package com.dfire.retail.app.common.event;

import android.view.View;

/**
 * 角色
 * @author <a href="mailto:zxh1000@163.com">张向华</a>.
 * @version $Revision: 1570 $
 * @create 2013-7-24 下午9:00:01
 */
public interface OnSampleItemClickListener {
	/**
	 * 点击事件.
	 * @param view 视图.
	 * @param obj 对象.
	 */
	void onSampleItemClick(String eventType,View view,Object obj);
}
