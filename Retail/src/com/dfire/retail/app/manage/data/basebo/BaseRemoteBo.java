package com.dfire.retail.app.manage.data.basebo;

import java.io.Serializable;

import com.dfire.retail.app.manage.util.StringUtils;
/**
 * 共同
 * @author ys
 *
 */
public class BaseRemoteBo implements Serializable{

	private static final long serialVersionUID = 8085779004485196501L;

	/**
	 * exceptionCode错误码 为null 的时候是操作成功。 不为null 的时候是返回错误信息
	 */
	private String exceptionCode;
	/**
	 * returnCode  返回消息 success：成功 fail：失败
	 */
	private String returnCode;
	/**
	 * @return the exceptionCode
	 */
	public String getExceptionCode() {
		return exceptionCode;
	}
	/**
	 * @param exceptionCode the exceptionCode to set
	 */
	public void setExceptionCode(String exceptionCode) {
		this.exceptionCode = exceptionCode;
	}
	/**
	 * @return the returnCode
	 */
	public String getReturnCode() {
		return returnCode;
	}
	/**
	 * @param returnCode the returnCode to set
	 */
	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}
	/**
	 * 判断是否 操作成功
	 * @return
	 */
	public boolean isSuccess(){
		if (StringUtils.isEmpty(returnCode)||StringUtils.isEquals(returnCode,"fail")) {
			return false;
		}else {
			return true;
		}
	}
}
