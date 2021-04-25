package org.forestcms.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("user_org")
public class UserOrg {
  
  @TableField(value = "id")
  private Long id;
  
  @TableField(value = "user_id")
  private Long userId;
  
  @TableField(value = "org_id")
  private Long orgId;
  
  @TableField(exist = false)
  private String orgName;

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

  public Long getOrgId() {
    return orgId;
  }

  public void setOrgId(Long orgId) {
    this.orgId = orgId;
  }

  public String getOrgName() {
    return orgName;
  }

  public void setOrgName(String orgName) {
    this.orgName = orgName;
  }
  
  
  
}
