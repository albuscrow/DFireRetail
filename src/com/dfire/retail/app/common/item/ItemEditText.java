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
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.dfire.retail.app.common.item.listener.IItemTextChangeListener;
import com.dfire.retail.app.manage.R;
import com.dfire.retail.app.manage.util.StringUtils;

/**
 * 表单项 ：输入框.
 * 
 * android:inputType参数类型说明 android:inputType="none"--输入普通字符
 * android:inputType="text"--输入普通字符
 * android:inputType="textCapCharacters"--输入普通字符
 * android:inputType="textCapWords"--单词首字母大小
 * android:inputType="textCapSentences"--仅第一个字母大小
 * android:inputType="textAutoCorrect"--前两个自动完成
 * android:inputType="textAutoComplete"--前两个自动完成
 * android:inputType="textMultiLine"--多行输入
 * android:inputType="textImeMultiLine"--输入法多行（不一定支持）
 * android:inputType="textNoSuggestions"--不提示 android:inputType="textUri"--URI格式
 * android:inputType="textEmailAddress"--电子邮件地址格式
 * android:inputType="textEmailSubject"--邮件主题格式
 * android:inputType="textShortMessage"--短消息格式
 * android:inputType="textLongMessage"--长消息格式
 * android:inputType="textPersonName"--人名格式
 * android:inputType="textPostalAddress"--邮政格式
 * android:inputType="textPassword"--密码格式
 * android:inputType="textVisiblePassword"--密码可见格式
 * android:inputType="textWebEditText"--作为网页表单的文本格式
 * android:inputType="textFilter"--文本筛选格式
 * android:inputType="textPhonetic"--拼音输入格式 android:inputType="number"--数字格式
 * android:inputType="numberSigned"--有符号数字格式
 * android:inputType="numberDecimal"--可以带小数点的浮点格式
 * android:inputType="phone"--拨号键盘 android:inputType="datetime"
 * android:inputType="date"--日期键盘 android:inputType="time"--时间键盘
 * 
 * @author <a href="mailto:zxh1000@163.com">张向华</a>.
 * @version $Revision: 1570 $
 * @date 2014年10月22日 上午10:05:16
 */
public class ItemEditText extends ItemBase {
	private String label;
	private String hit;

	private TextView lblName;
	private ImageView img;
	private Button btn;
	private EditText lblVal;
	private TextView lblHit;

	/**
	 * <code>注册事件处理</code>.
	 */
	private IItemTextChangeListener itemChange;

	/**
	 * @param context
	 * @param attrs
	 */
	public ItemEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.globe_item_edit_text, this);
		initMainView();
		saveTag.setVisibility(View.GONE);
	}

	/**
	 * 初始化控件.
	 * 
	 * @param label
	 *            表单项名称.
	 * @param hit
	 *            提示文字.
	 * @param inputType
	 *            键盘类型：参看类注释，都从InputType引入.
	 */
	public void initLabel(String label, String hit, int inputType) {
		initLabel(label, hit, Boolean.FALSE, inputType);
	}

	/**
	 * 文本内容变化监听接口.
	 * @param itemChange 文本内容变化监听接口.
	 */
	public void setTextChangeListener(IItemTextChangeListener itemChange) {
		this.itemChange=itemChange;
	}

	/**
	 * 初始化控件.
	 * 
	 * @param label
	 *            表单项名称.
	 * @param hit
	 *            提示文字.
	 * @param isRequest
	 *            是否必填.
	 * @param inputType
	 *            键盘理性，数字例子：InputType.TYPE_CLASS_NUMBER，
	 */
	public void initLabel(String label, String hit, Boolean isRequest,
			int inputType) {
		lblName.setText(StringUtils.isEmpty(label) ? "" : label);
		lblHit.setHint(StringUtils.isEmpty(hit) ? "" : hit);
		lblHit.setVisibility(StringUtils.isEmpty(hit) ? View.GONE
				: View.VISIBLE);
		lblVal.setHint(isRequest ? "必填" : "可不填");
		lblVal.setHintTextColor(isRequest ? Color.RED : Color.GRAY);
		lblVal.setInputType(inputType);

		lblVal.addTextChangedListener(new TextWatcher() {

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
					itemChange.onItemEditTextChange(ItemEditText.this);
				}
				
			}
		});
	}

	/**
	 * 初始化控件的值.
	 * 
	 * @param val
	 *            值.
	 */
	public void initData(String val) {
		setOldVal(StringUtils.isEmpty(val) ? "" : val);
		changeData(val);
	}

	/**
	 * 第2次赋值，修改值.
	 * 
	 * @param val
	 *            值.
	 */
	public void changeData(String val) {
		setCurrVal(StringUtils.isEmpty(val) ? "" : val);
		lblVal.setText(StringUtils.isEmpty(val) ? "" : val);
		isChange();
	}

	/**
	 * 得到当前的值.
	 * 
	 * @return
	 */
	public String getStrVal() {
		return getCurrVal();
	}

	/**
	 * 清除表单的未保存标记.
	 */
	public void clearChange() {
		setOldVal(getCurrVal());
		isChange();
	}

	/**
	 * 初始化.
	 */
	private void initMainView() {
		lblName = (TextView) this.findViewById(R.id.lblName);
		lblVal = (EditText) this.findViewById(R.id.lblVal);
		lblHit = (TextView) this.findViewById(R.id.lblHit);
		saveTag = (TextView) this.findViewById(R.id.saveTag);
		img = (ImageView) this.findViewById(R.id.img);
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
	 * 得到值处理.
	 * 
	 * @return 值处理.
	 */
	public EditText getLblVal() {
		return lblVal;
	}

}