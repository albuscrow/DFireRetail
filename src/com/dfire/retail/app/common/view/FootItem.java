/************************* 版权声明 **********************************
 * 版权所有：Copyright (c) 2008, 2014 张向华
 *
 * 工程名称：	Retail
 * 创建者：	Administrator 创建日期： 2014年10月23日
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


package com.dfire.retail.app.common.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.dfire.retail.app.common.item.listener.IFootItemClickListener;
import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.util.StringUtils;

/**
 * 页脚按钮.
 * @author <a href="mailto:zxh1000@163.com">张向华</a>.
 * @version $Revision: 1570 $
 * @date 2014年10月23日 下午3:10:58
 */
public class FootItem extends FrameLayout implements  View.OnClickListener{
  
	private ImageView imgBg;
	private ImageView imgIcon;
	private TextView btnName;
	private Button btn;
	
	private IFootItemClickListener itemClick;
	/**
	 * 构造函数.
	 * @param context
	 * @param attrs
	 */
	public FootItem(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.foot_item_view, this);
		initMainView();
	}
	/**
	 * 主界面.
	 */
	private void initMainView() {
		this.imgBg=(ImageView)findViewById(R.id.imgBg);
		this.imgIcon=(ImageView)findViewById(R.id.imgIcon);
		this.btnName=(TextView)findViewById(R.id.btnName);
		this.btn=(Button)findViewById(R.id.btn);
		this.btn.setOnClickListener(this);
	}
	
	/**
	 * 初始化界面.
	 * @param imgBg
	 * @param imgIcon
	 * @param btnName
	 * @param itemClick
	 */
	public void initUI(int imgBg,int imgIcon,String btnName){
		this.imgBg.setImageDrawable(getResources().getDrawable(imgBg));
		this.imgIcon.setImageDrawable(getResources().getDrawable(imgIcon));
		this.btnName.setText(StringUtils.isEmpty(btnName)?"":btnName);
		
	}
	
	public void initListener(IFootItemClickListener itemClick){
		this.itemClick=itemClick;
	}
	@Override
	public void onClick(View arg0) {
		this.itemClick.onFootItemClick(this);
		
	}

}
