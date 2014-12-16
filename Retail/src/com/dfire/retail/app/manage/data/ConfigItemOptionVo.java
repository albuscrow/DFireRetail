package com.dfire.retail.app.manage.data;

import java.io.Serializable;
import java.math.BigDecimal;

import android.R.integer;

/**
 * 
 * @author kyolee
 *
 */
public class ConfigItemOptionVo implements Serializable {
	String value;
	String name;
	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}
	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
}
