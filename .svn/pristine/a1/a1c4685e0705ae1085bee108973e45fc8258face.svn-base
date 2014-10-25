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
import android.widget.LinearLayout;

import com.dfire.retail.app.common.item.listener.IFootItemClickListener;
import com.dfire.retail.app.common.item.listener.IHelpClickEvent;
import com.dfire.retail.app.manage.R;

/**
 * 扫描页脚.
 * 
 * @author <a href="mailto:zxh1000@163.com">张向华</a>.
 * @version $Revision: 1570 $
 * @date 2014年10月23日 下午1:48:21
 */
public class FootScanView extends LinearLayout implements View.OnClickListener {

	private Button btnHelp;

	private FootItem btnScan;
	private IFootItemClickListener itemClick;
	private IHelpClickEvent helpClickEvent;

	/**
	 * @param context
	 * @param attrs
	 */
	public FootScanView(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.globe_foot_scan_view, this);
		initMainView();

	}

	private void initMainView() {
		btnHelp = (Button) findViewById(R.id.btnHelp);

		btnScan = (FootItem) findViewById(R.id.btnScan);
		btnScan.initUI(R.drawable.ico_rnd_red2, R.drawable.ico_saoyisao, "扫一扫");
	}

	public void initListener(IFootItemClickListener itemClick,
			IHelpClickEvent helpClickEvent) {
		this.itemClick = itemClick;
		this.helpClickEvent = helpClickEvent;
		btnScan.initListener(this.itemClick);
	}

	@Override
	public void onClick(View arg0) {
		if (this.helpClickEvent != null) {
			this.helpClickEvent.onHelpClick(this.btnHelp);
		}

	}

}
