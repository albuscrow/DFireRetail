package com.dfire.retail.app.manage.data;

import java.io.Serializable;
import java.util.List;
/**
 * 项目名称：Retail  
 * 类名称：SystemInfoVo  
 * 类描述：   角色权限-系统
 * 创建时间：2014年11月22日 下午4:36:42  
 * @author chengzi  
 * @version 1.0
 */
public class SystemInfoVo implements Serializable {

    private static final long serialVersionUID = -7610252380420260720L;
    private int systemId;
    private String systemInfoId;
    private String systemName;
    private String systemCode;
    private String systemType;
    
    private boolean choiceFlag;
    private boolean oldChoiceFlag;
    
    private List<ModuleVo> moduleVoList;

    public int getSystemId() {
        return systemId;
    }

    public void setSystemId(int systemId) {
        this.systemId = systemId;
    }

    public String getSystemInfoId() {
        return systemInfoId;
    }

    public void setSystemInfoId(String systemInfoId) {
        this.systemInfoId = systemInfoId;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public String getSystemCode() {
        return systemCode;
    }

    public void setSystemCode(String systemCode) {
        this.systemCode = systemCode;
    }

    public String getSystemType() {
        return systemType;
    }

    public void setSystemType(String systemType) {
        this.systemType = systemType;
    }

    public List<ModuleVo> getModuleVoList() {
        return moduleVoList;
    }

    public void setModuleVoList(List<ModuleVo> moduleVoList) {
        this.moduleVoList = moduleVoList;
    }

    public boolean isChoiceFlag() {
        return choiceFlag;
    }

    public void setChoiceFlag(boolean choiceFlag) {
        this.choiceFlag = choiceFlag;
    }

    public boolean isOldChoiceFlag() {
        return oldChoiceFlag;
    }

    public void setOldChoiceFlag(boolean oldChoiceFlag) {
        this.oldChoiceFlag = oldChoiceFlag;
    }            

    
}
