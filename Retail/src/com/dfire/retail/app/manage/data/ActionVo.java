package com.dfire.retail.app.manage.data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import android.R.integer;

/**
 * 用户权限
 * @author kyolee
 *
 */
public class ActionVo implements Serializable {

    private static final long serialVersionUID = 7023294206316501851L;
    private String actionId;//权限ID
	private String actionName;//权限名
	private Integer actionType;//1：功能权限  2：数据权限 3：增值服务权限"		
	private String code;//代码
	private integer isMenu;//是否是菜单
	private String urlPath;//入口路径
	
	private Integer actionDataType;
	private Boolean choiceFlag;
	
	private List<DicVo> dicVoList;          
	
	private String actionDataName;
	private Boolean oldChoiceFlag;
	private Integer oldActionDataType;
	private Boolean originChoiceFlag;
	private Integer originActionDataType;
	public String getActionId() {
		return actionId;
	}
	public void setActionId(String actionId) {
		this.actionId = actionId;
	}
	public String getActionName() {
		return actionName;
	}
	public void setActionName(String actionName) {
		this.actionName = actionName;
	}
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public integer getIsMenu() {
		return isMenu;
	}
	public void setIsMenu(integer isMenu) {
		this.isMenu = isMenu;
	}
	public String getUrlPath() {
		return urlPath;
	}
	public void setUrlPath(String urlPath) {
		this.urlPath = urlPath;
	}
    public Integer getActionType() {
        return actionType;
    }
    public void setActionType(Integer actionType) {
        this.actionType = actionType;
    }
    public Integer getActionDataType() {
        return actionDataType;
    }
    public void setActionDataType(Integer actionDataType) {
        this.actionDataType = actionDataType;
    }
    public Boolean getChoiceFlag() {
        return choiceFlag;
    }
    public void setChoiceFlag(Boolean choiceFlag) {
        this.choiceFlag = choiceFlag;
    }
    public List<DicVo> getDicVoList() {
        return dicVoList;
    }
    public void setDicVoList(List<DicVo> dicVoList) {
        this.dicVoList = dicVoList;
    }
    public String getActionDataName() {
        return actionDataName;
    }
    public void setActionDataName(String actionDataName) {
        this.actionDataName = actionDataName;
    }
    public Boolean getOldChoiceFlag() {
        return oldChoiceFlag;
    }
    public void setOldChoiceFlag(Boolean oldChoiceFlag) {
        this.oldChoiceFlag = oldChoiceFlag;
    }
    public Integer getOldActionDataType() {
        return oldActionDataType;
    }
    public void setOldActionDataType(Integer oldActionDataType) {
        this.oldActionDataType = oldActionDataType;
    }
    public Boolean getOriginChoiceFlag() {
        return originChoiceFlag;
    }
    public void setOriginChoiceFlag(Boolean originChoiceFlag) {
        this.originChoiceFlag = originChoiceFlag;
    }
    public Integer getOriginActionDataType() {
        return originActionDataType;
    }
    public void setOriginActionDataType(Integer originActionDataType) {
        this.originActionDataType = originActionDataType;
    }

	
}
