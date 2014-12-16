/**
 * 
 */
package com.dfire.retail.app.common.item;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.dfire.retail.app.manage.common.CommonUtils;

/**
 * @author 李锦运 2014-10-26
 */
public class DateUtil {
	public final static String YMDHMS_EN = "yyyy-MM-dd HH:mm:ss";
	public final static String YMDHM_EN = "yyyy-MM-dd HH:mm";
	public final static String YMD_EN = "yyyy-MM-dd";
	public final static String MDHM_EN = "MM-dd HH:mm";
	public final static String HM_EN = "HH:mm";

	public final static String YMDHMS_ZH_EN = "yyyy年MM月dd日 HH:mm:ss";
	public final static String YMDHM_ZH_EN = "yyyy年MM月dd日 HH:mm";
	public final static String MDHM_ZH_EN = "MM月dd日 HH:mm";

	public final static String YMDHMS_ZH = "yyyy年MM月dd日 HH时mm分ss";
	public final static String YMDHM_ZH = "yyyy年MM月dd日 HH时mm分";
	public final static String YMD_ZH = "yyyy年MM月dd日";
	public final static String MDHM_ZH = "MM月dd日 HH时mm分";

	/**
	 * 将日期转化为字符串
	 * 
	 * @param time
	 * @return
	 */
	public static String timeToStrYMDHMS_EN(long time) {
		if (time == 0) {
			return "";
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat(YMDHMS_EN);
		return dateFormat.format(new Date(time));
	}

	public static String timeToStrYMDHMS_EN(Date date) {
		if (date == null) {
			return "";
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat(YMDHMS_EN);
		return dateFormat.format(date);
	}

	public static String timeToStrYMDHM_EN(long time) {
		if (time == 0) {
			return "";
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat(YMDHM_EN);
		return dateFormat.format(new Date(time));
	}

	public static String timeToStrYMDHM_EN(Date date) {
		if (date == null) {
			return "";
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat(YMDHM_EN);
		return dateFormat.format(date);
	}

	public static String timeToStrYMD_EN(long time) {
		if (time == 0) {
			return "";
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat(YMD_EN);
		return dateFormat.format(new Date(time));
	}

	public static String timeToStrYMD_EN(Date date) {
		if (date == null) {
			return "";
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat(YMD_EN);
		return dateFormat.format(date);
	}

	public static String timeToStrMDHM_EN(long time) {
		if (time == 0) {
			return "";
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat(MDHM_EN);
		return dateFormat.format(new Date(time));
	}

	public static String timeToStrMDHM_EN(Date date) {
		if (date == null) {
			return "";
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat(MDHM_EN);
		return dateFormat.format(date);
	}

	public static String timeToStrHM_EN(long time) {
		if (time == 0) {
			return "";
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat(HM_EN);
		return dateFormat.format(new Date(time));
	}

	public static String timeToStrHM_EN(Date date) {
		if (date == null) {
			return "";
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat(HM_EN);
		return dateFormat.format(date);
	}

	public static String timeToStrYMDHMS_ZH_EN(long time) {
		if (time == 0) {
			return "";
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat(YMDHMS_ZH_EN);
		return dateFormat.format(new Date(time));
	}

	public static String timeToStrYMDHMS_ZH_EN(Date date) {
		if (date == null) {
			return "";
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat(YMDHMS_ZH_EN);
		return dateFormat.format(date);
	}

	public static String timeToStrYMDHM_ZH_EN(long time) {
		if (time == 0) {
			return "";
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat(YMDHM_ZH_EN);
		return dateFormat.format(new Date(time));
	}

	public static String timeToStrYMDHM_ZH_EN(Date date) {
		if (date == null) {
			return "";
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat(YMDHM_ZH_EN);
		return dateFormat.format(date);
	}

	public static String timeToStrMDHM_ZH_EN(long time) {
		if (time == 0) {
			return "";
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat(MDHM_ZH_EN);
		return dateFormat.format(new Date(time));
	}

	public static String timeToStrMDHM_ZH_EN(Date date) {
		if (date == null) {
			return "";
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat(MDHM_ZH_EN);
		return dateFormat.format(date);
	}

	public static String timeToStrYMDHMS_ZH(long time) {
		if (time == 0) {
			return "";
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat(YMDHMS_ZH);
		return dateFormat.format(new Date(time));
	}

	public static String timeToStrYMDHMS_ZH(Date date) {
		if (date == null) {
			return "";
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat(YMDHMS_ZH);
		return dateFormat.format(date);
	}

	public static String timeToStrYMDHM_ZH(long time) {
		if (time == 0) {
			return "";
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat(YMDHM_ZH);
		return dateFormat.format(new Date(time));
	}

	public static String timeToStrYMDHM_ZH(Date date) {
		if (date == null) {
			return "";
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat(YMDHM_ZH);
		return dateFormat.format(date);
	}

	public static String timeToStrYMD_ZH(long time) {
		if (time == 0) {
			return "";
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat(YMD_ZH);
		return dateFormat.format(new Date(time));
	}

	public static String timeToStrYMD_ZH(Date date) {
		if (date == null) {
			return "";
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat(YMD_ZH);
		return dateFormat.format(date);
	}

	public static String timeToStrMDHM_ZH(long time) {
		if (time == 0) {
			return "";
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat(MDHM_ZH);
		return dateFormat.format(new Date(time));
	}

	public static String timeToStrMDHM_ZH(Date date) {
		if (date == null) {
			return "";
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat(MDHM_ZH);
		return dateFormat.format(date);
	}

	/**
	 * 字符串得到日期
	 * 
	 * @param time
	 * @return
	 */
	public static Date strToDateYMDHMS_EN(String time) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(YMDHMS_EN);
		try {
			return dateFormat.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Date strToDateYMDHM_EN(String time) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(YMDHM_EN);
		try {
			return dateFormat.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Date strToDateYMD_EN(String time) {
		if(CommonUtils.isEmpty(time)) return null;
		SimpleDateFormat dateFormat = new SimpleDateFormat(YMD_EN);
		try {
			return dateFormat.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Date strToDateMDHM_EN(String time) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(MDHM_EN);
		try {
			return dateFormat.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Date strToDateHM_EN(String time) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(HM_EN);
		try {
			return dateFormat.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Date strToDateYMDHMS_ZH_EN(String time) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(YMDHMS_ZH_EN);
		try {
			return dateFormat.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Date strToDateYMDHM_ZH_EN(String time) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(YMDHM_ZH_EN);
		try {
			return dateFormat.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Date strToDateMDHM_ZH_EN(String time) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(MDHM_ZH_EN);
		try {
			return dateFormat.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Date strToDateYMDHMS_ZH(String time) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(YMDHMS_ZH);
		try {
			return dateFormat.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Date strToDateYMDHM_ZH(String time) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(YMDHM_ZH);
		try {
			return dateFormat.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Date strToDateYMD_ZH(String time) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(YMD_ZH);
		try {
			return dateFormat.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Date strToDateMDHM_ZH(String time) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(MDHM_ZH);
		try {
			return dateFormat.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
}
