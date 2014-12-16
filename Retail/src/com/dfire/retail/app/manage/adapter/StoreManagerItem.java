package com.dfire.retail.app.manage.adapter;
/**
 * 更多信息中的功能项
 * @author kyolee
 *
 */
public class StoreManagerItem {
	
	  private int imageId;
	  private String mainText;
	  private String subheadText;
	  private Class<?> destClass;
	  
	  public StoreManagerItem(int imageView,String mainText,String subheadText,Class<?> destClass){
		  this.imageId = imageView;
		  this.mainText = mainText;
		  this.subheadText = subheadText;
		  this.destClass = destClass;
		  
	  }
	  
	public Class<?> getDestClass() {
		return destClass;
	}

	public void setDestClass(Class<?> destClass) {
		this.destClass = destClass;
	}

	public int getImageId() {
		return imageId;
	}
	public void setImageId(int imageView) {
		this.imageId = imageView;
	}
	public String getMainText() {
		return mainText;
	}
	public void setMainText(String mainText) {
		this.mainText = mainText;
	}
	public String getSubheadText() {
		return subheadText;
	}
	public void setSubheadText(String subheadText) {
		this.subheadText = subheadText;
	}
}
