package com.dfire.retail.app.manage.adapter;

public class DateItem {
	private String title;
	private boolean ischeck;
	
	public DateItem(String title,boolean ischeck){
		this.title = title;
		this.ischeck = ischeck;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public boolean isIscheck() {
		return ischeck;
	}

	public void setIscheck(boolean ischeck) {
		this.ischeck = ischeck;
	}
	
	

}
