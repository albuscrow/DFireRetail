package com.dfire.retail.app.manage.adapter;

import java.io.Serializable;
import java.util.ArrayList;

public class RolePermissionItem implements Serializable {
    private static final long serialVersionUID = 1L;
    public int mPosition;
    public String mName;
    public int mTag;//跳转目标
    public ArrayList<RolePermissionSubItem> mChoice;
    public int mId;/**/
    public boolean mPreOrBg = true;/*true:代表前台权限，false:后台权限设置*/
    
    public RolePermissionItem(int position, String name, int tag, int id, boolean preOrBg) {
        mPosition = position;
        mName = name;
        mTag = tag;
        mId = id;
        mPreOrBg = preOrBg;
    }
    
}
