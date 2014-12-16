package com.dfire.retail.app.manage.adapter;

public class MoreInfoItem {
	private int imageId;
	private String Text;
	
	public MoreInfoItem(int imageId,String Text){
		this.imageId = imageId;
		this.Text = Text;
	}

	public int getImageId() {
		return imageId;
	}

	public void setImageId(int imageId) {
		this.imageId = imageId;
	}

	public String getText() {
		return Text;
	}

	public void setText(String text) {
		Text = text;
	}
	
	

}
