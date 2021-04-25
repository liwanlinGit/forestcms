package org.forestcms.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("sys_role_permissions")
public class SysRolePermissions {
    
  @TableField(value = "id")
  private Long id;
  
  @TableField(value = "role_id")
  private Long roleId;
  
  @TableField(value = "permissions_id")
  private Long permissionsId;
  
  @TableField(exist = false)
  private String permissionsIds;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getRoleId() {
    return roleId;
  }

  public void setRoleId(Long roleId) {
    this.roleId = roleId;
  }

  public Long getPermissionsId() {
    return permissionsId;
  }

  public void setPermissionsId(Long permissionsId) {
    this.permissionsId = permissionsId;
  }

  public String getPermissionsIds() {
    return permissionsIds;
  }

  public void setPermissionsIds(String permissionsIds) {
    this.permissionsIds = permissionsIds;
  }
  
  
  
}
