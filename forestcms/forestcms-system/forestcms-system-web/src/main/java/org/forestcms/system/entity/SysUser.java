package org.forestcms.system.entity;

import java.io.Serializable;
import java.util.List;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("sys_user")
public class SysUser implements Serializable {

  @TableField(value = "id")
  private Long id;

  @TableField(value = "login_name")
  private String loginName;

  @TableField(value = "password")
  private String password;

  @TableField(value = "create_time", fill = FieldFill.INSERT)
  private String createTime;

  @TableField(value = "update_time", fill = FieldFill.UPDATE)
  private String updateTime;

  @TableField(value = "is_delete")
  @TableLogic
  private Integer isDelete;

  @TableField(value = "is_status")
  private Integer isStatus;

  @TableField(value = "name")
  private String name;

  @TableField(value = "login_time")
  private String loginTime;

  @TableField(value = "phone")
  private String phone;

  @TableField(value = "email")
  private String email;

  @TableField(value = "ip")
  private String ip;
  
  @TableField(value = "type")
  private Integer type;
  


  @TableField(exist = false)
  private String roleIds;
  
  @TableField(exist = false)
  private String roleNames;
  
  @TableField(exist = false)
  private String orgIds;
  
  @TableField(exist = false)
  private String orgNames;
  
  @TableField(exist = false)
  private List<SysRole> sysRoleList;
  
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getLoginName() {
    return loginName;
  }

  public void setLoginName(String loginName) {
    this.loginName = loginName;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getCreateTime() {
    return createTime;
  }

  public void setCreateTime(String createTime) {
    this.createTime = createTime;
  }

  public String getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(String updateTime) {
    this.updateTime = updateTime;
  }

  public Integer getIsDelete() {
    return isDelete;
  }

  public void setIsDelete(Integer isDelete) {
    this.isDelete = isDelete;
  }

  public Integer getIsStatus() {
    return isStatus;
  }

  public void setIsStatus(Integer isStatus) {
    this.isStatus = isStatus;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getLoginTime() {
    return loginTime;
  }

  public void setLoginTime(String loginTime) {
    this.loginTime = loginTime;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getIp() {
    return ip;
  }

  public void setIp(String ip) {
    this.ip = ip;
  }

  public Integer getType() {
    return type;
  }

  public void setType(Integer type) {
    this.type = type;
  }

  

  public String getRoleIds() {
    return roleIds;
  }

  public void setRoleIds(String roleIds) {
    this.roleIds = roleIds;
  }

  public String getRoleNames() {
    return roleNames;
  }

  public void setRoleNames(String roleNames) {
    this.roleNames = roleNames;
  }

  public List<SysRole> getSysRoleList() {
    return sysRoleList;
  }

  public void setSysRoleList(List<SysRole> sysRoleList) {
    this.sysRoleList = sysRoleList;
  }

  public String getOrgIds() {
    return orgIds;
  }

  public void setOrgIds(String orgIds) {
    this.orgIds = orgIds;
  }

  public String getOrgNames() {
    return orgNames;
  }

  public void setOrgNames(String orgNames) {
    this.orgNames = orgNames;
  }
  
}
