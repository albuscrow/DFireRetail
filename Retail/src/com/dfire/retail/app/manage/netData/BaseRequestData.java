package com.dfire.retail.app.manage.netData;

/**
 * 共通参数
 * @author qiuch
 *
 */
public class BaseRequestData {
	private String sessionId;
	private String version;

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

}
