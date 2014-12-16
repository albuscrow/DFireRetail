package com.dfire.retail.app.manage.adapter;

public class CommonItem {
	
	String infotitle;
	String infomsg1;
	String infomsg2;
	
	public CommonItem(String infotitle,String infomsg1,String infomsg2){
		this.infotitle = infotitle;
		this.infomsg1 = infomsg1;
		this.infomsg2 = infomsg2;
	}

	public String getInfotitle() {
		return infotitle;
	}

	public void setInfotitle(String infotitle) {
		this.infotitle = infotitle;
	}

	public String getInfomsg1() {
		return infomsg1;
	}

	public void setInfomsg1(String infomsg1) {
		this.infomsg1 = infomsg1;
	}

	public String getInfomsg2() {
		return infomsg2;
	}

	public void setInfomsg2(String infomsg2) {
		this.infomsg2 = infomsg2;
	}
	
}
