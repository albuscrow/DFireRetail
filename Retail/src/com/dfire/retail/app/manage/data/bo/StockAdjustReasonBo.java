package com.dfire.retail.app.manage.data.bo;

import java.util.List;
import java.util.Map;

import com.dfire.retail.app.manage.data.basebo.BaseRemoteBo;
/**
 * 得到调整原因   or 供应商类型
 * @author ys
 *
 */
public class StockAdjustReasonBo extends BaseRemoteBo{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5070746264634268852L;

	/**
	 * map里的key值是typeName   typeVal
	 * typeName 表示类型名字  typeVal表示调整原因编号
	 */
	private List<Map<String, String>> listMap;

	public List<Map<String, String>> getListMap() {
		return listMap;
	}

	public void setListMap(List<Map<String, String>> listMap) {
		this.listMap = listMap;
	}
	
}
