package com.dfire.retail.app.manage.network;

/**
 * 网络请求的回调接口
 */
public interface RequestResultCallback
{
    /**
     * 数据请求成功
     * @param str
     */
    public void onSuccess(String str);
    
	/**
     * 数据请求失败
     * @param e
     */        
    public void onFail(Exception e);
}
