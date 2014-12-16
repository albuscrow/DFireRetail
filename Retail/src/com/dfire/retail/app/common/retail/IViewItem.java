package com.dfire.retail.app.common.retail;

import android.view.View;
/**
 * 项目名称：retail  
 * 类名称：IViewItem  
 * 类描述：   显示层子元素接口，继承自IView
 * 创建时间：2014年10月22日 上午10:42:18  
 * @author ys  
 * @version 1.0
 */
public interface IViewItem extends IView
{
	public void itemSelect();
	
	public View getItemView();
}
