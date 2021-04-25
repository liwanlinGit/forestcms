package org.forestcms.system.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;


@TableName("sys_user_role")
public class SysUserRole implements Serializable {

  @TableField(value = "id")
  private Long id;
  
  @TableField(value = "user_id")
  private Long userId;
  
  @TableField(value = "role_id")
  private Long roleId;
  
  @TableField(exist = false)
  private String roleIds;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public Long getRoleId() {
    return roleId;
  }

  public void setRoleId(Long roleId) {
    this.roleId = roleId;
  }

  public String getRoleIds() {
    return roleIds;
  }

  public void setRoleIds(String roleIds) {
    this.roleIds = roleIds;
  }
  
  
}
