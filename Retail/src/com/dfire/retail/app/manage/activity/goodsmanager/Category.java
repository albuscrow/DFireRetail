package com.dfire.retail.app.manage.activity.goodsmanager;

import java.io.Serializable;

import com.dfire.retail.app.manage.data.CategoryVo;

/**
 * The Class Category.
 * 
 * @author albuscrow
 */
public class Category implements Serializable{
	
	/** The name. */
	public String name;
	
	/** The id. */
	public String id;
	
	/** The parents. */
	public String parents;
	
	/** The depth. */
	public int depth;
	
	/** The original. */
	public CategoryVo original;
	
	/** The parent name. */
	public String parentName;
	
	public Integer goodsSum;
	
	
}
