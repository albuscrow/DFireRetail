/**
 * 
 */
package com.dfire.retail.app.manage.netData;

/**
 * 共通返回参数
 * 
 * @author qiuch
 * 
 */
public class BaseResult {
	private String exceptionCode;
	private String exceptionMessage;
	private String returnCode;

	public String getExceptionCode() {
		return exceptionCode;
	}

	public void setExceptionCode(String exceptionCode) {
		this.exceptionCode = exceptionCode;
	}

	public String getExceptionMessage() {
		return exceptionMessage;
	}

	public void setExceptionMessage(String exceptionMessage) {
		this.exceptionMessage = exceptionMessage;
	}

	public String getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}

}
