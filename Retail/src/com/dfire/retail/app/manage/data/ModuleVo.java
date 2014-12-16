package com.dfire.retail.app.manage.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
/**
 * 项目名称：Retail  
 * 类名称：ModuleVo  
 * 类描述：   角色权限模块
 * 创建时间：2014年11月22日 下午4:35:59  
 * @author chengzi  
 * @version 1.0
 */
public class ModuleVo implements Serializable {

    private static final long serialVersionUID = -5955152536644079353L;
    private String moduleId;
    private String moduleName;
    private Integer count;
    private String actionNameOfModule;
    private ArrayList<ActionVo> actionVoList;
    
    private SystemInfoVo systemInfoVo;//该模块所属的系统权限
    private Integer oldCount;
    private boolean changed;
    public String getModuleId() {
        return moduleId;
    }
    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }
    public String getModuleName() {
        return moduleName;
    }
    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }
    public Integer getCount() {
        return count;
    }
    public void setCount(Integer count) {
        this.count = count;
    }
    public String getActionNameOfModule() {
        return actionNameOfModule;
    }
    public void setActionNameOfModule(String actionNameOfModule) {
        this.actionNameOfModule = actionNameOfModule;
    }
    public ArrayList<ActionVo> getActionVoList() {
        return actionVoList;
    }
    public void setActionVoList(ArrayList<ActionVo> actionVoList) {
        this.actionVoList = actionVoList;
    }
    public SystemInfoVo getSystemInfoVo() {
        return systemInfoVo;
    }
    public void setSystemInfoVo(SystemInfoVo systemInfoVo) {
        this.systemInfoVo = systemInfoVo;
    }
    public Integer getOldCount() {
        return oldCount;
    }
    public void setOldCount(Integer oldCount) {
        this.oldCount = oldCount;
    }
    public boolean isChanged() {
        return changed;
    }
    public void setChanged(boolean changed) {
        this.changed = changed;
    }
    

    public boolean isModuleChanged() {
        if(this.actionVoList != null) {
            for(ActionVo action : this.actionVoList) {
                if(!action.getChoiceFlag() == action.getOriginChoiceFlag()) {
                    return true;
                }
                if(action.getActionDataType() != null && action.getOriginActionDataType() != null && !(action.getActionDataType().intValue() == action.getOriginActionDataType().intValue())) {
                    return true;
                }
            }
        }
        return false;
    }
}
