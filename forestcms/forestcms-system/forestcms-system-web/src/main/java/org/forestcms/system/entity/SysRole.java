package org.forestcms.system.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("sys_role")
public class SysRole {

  @TableField(value = "id")
  private Long id;

  @TableField(value = "role_name")
  private String roleName;

  @TableField(value = "role_code")
  private String roleCode;

  @TableField(value = "is_admin")
  private String isAdmin;

  @TableField(value = "is_built_in")
  private String isBuiltIn;

  @TableField(value = "create_time", fill = FieldFill.INSERT)
  private String createTime;

  @TableField(value = "update_time", fill = FieldFill.UPDATE)
  private String updateTime;

  @TableField(value = "is_delete")
  @TableLogic
  private Integer is_delete;
  
  @TableField(exist = false)
  private Boolean checked;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getRoleName() {
    return roleName;
  }

  public void setRoleName(String roleName) {
    this.roleName = roleName;
  }

  public String getRoleCode() {
    return roleCode;
  }

  public void setRoleCode(String roleCode) {
    this.roleCode = roleCode;
  }

  public String getIsAdmin() {
    return isAdmin;
  }

  public void setIsAdmin(String isAdmin) {
    this.isAdmin = isAdmin;
  }

  public String getIsBuiltIn() {
    return isBuiltIn;
  }

  public void setIsBuiltIn(String isBuiltIn) {
    this.isBuiltIn = isBuiltIn;
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

  public Integer getIs_delete() {
    return is_delete;
  }

  public void setIs_delete(Integer is_delete) {
    this.is_delete = is_delete;
  }

  public Boolean getChecked() {
    return checked;
  }

  public void setChecked(Boolean checked) {
    this.checked = checked;
  }

}
