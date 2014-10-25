package com.dfire.retail.app.manage.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 字符串处理工具.
 * @author <a href="mailto:zxh1000@163.com">张向华</a>.
 * @version $Revision: 1570 $
 * @create 2013-7-22 上午10:32:12
 */
public class StringUtils {
	private final static ThreadLocal<SimpleDateFormat> dateFormater = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		}
	};

	private final static ThreadLocal<SimpleDateFormat> dateFormater2 = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd");
		}
	};
	
	/**
	 * 将字符串转位日期类型
	 * @param sdate
	 * @return
	 */
	public static Date toDate(String sdate) {
		try {
			return dateFormater.get().parse(sdate);
		} catch (ParseException e) {
			return null;
		}
	}
	/**
	 * 判断字符串是否为空.
	 * 
	 * @param str
	 *            字符串.
	 * @return 是否为空.
	 */
	public static boolean isEmpty(String str) {
		return str == null || str.trim().length() == 0;
	}
	
	
	/**
	 * 把空数据转成null.
	 * 
	 * @param source
	 *            原始值.
	 * @return 转换后的值.
	 */
	public static String emptyToNull(String source) {
		return isEmpty(source) ? null : source;
	}
	
	/**
	 * 把null转换称空串.
	 * 
	 * @param source
	 *            原始串.
	 * @return
	 */
	public static String nullToEmpty(String source) {
		return isEmpty(source) ? "" : source;
	}
	
	/**
	 * 字符串是否相等.
	 * 
	 * @param source
	 *            原始.
	 * @param target
	 *            目标.
	 * @return 是否相等.
	 */
	public static boolean isEquals(String source, String target) {
		if (isEmpty(source)) {
			return isEmpty(target);
		} else {
			return source.equals(target);
		}
	}
	
//	/**
//	 * 取得简拼
//	 * 
//	 * @param name
//	 */
//	public static final String getSpell(String name) {
//		if (!isEmpty(name)) {
//			String pyStr = PYUtil.getPYString(name);
//			if (!isEmpty(pyStr)) {
//				return pyStr.toUpperCase();
//			}
//		}
//		return null;
//	}
}
