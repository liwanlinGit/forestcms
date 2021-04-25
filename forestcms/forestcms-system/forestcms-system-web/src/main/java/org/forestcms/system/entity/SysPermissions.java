package org.forestcms.system.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("sys_permissions")
public class SysPermissions implements Serializable {

	@TableField(value = "id")
	private Long id;

	@TableField(value = "create_time", fill = FieldFill.INSERT)
	private String createTime;

	@TableField(value = "update_time", fill = FieldFill.UPDATE)
	private String updateTime;

	@TableField(value = "name")
	private String name;

	@TableField(value = "parent_id")
	private Long parentId;

	@TableField(value = "tree_depth")
	private Integer treeDepth;

	@TableField(value = "type")
	private Integer type;

	@TableField(value = "url")
	private String url;

	@TableField(value = "url_type")
	private String urlType;

	@TableField(value = "priority")
	private Integer priority;

	@TableField(value = "icon_name")
	private String iconName;

	@TableField(value = "is_delete")
	@TableLogic
	private Integer isDelete;

	@TableField(value = "is_parent")
	private String isParent;

	@TableField(exist = false)
	private boolean hasChildren;

	@TableField(value = "parent_name")
	private String parentName;

	@TableField(exist = false)
	private Boolean checked;

	@TableField(exist = false)
	private String roleCode;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public Integer getTreeDepth() {
		return treeDepth;
	}

	public void setTreeDepth(Integer treeDepth) {
		this.treeDepth = treeDepth;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public String getIconName() {
		return iconName;
	}

	public void setIconName(String iconName) {
		this.iconName = iconName;
	}

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

	public String getIsParent() {
		return isParent;
	}

	public void setIsParent(String isParent) {
		this.isParent = isParent;
	}

	public boolean getHasChildren() {
		return Boolean.parseBoolean(getIsParent());
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public Boolean getChecked() {
		return checked;
	}

	public void setChecked(Boolean checked) {
		this.checked = checked;
	}

	public String getUrlType() {
		return urlType;
	}

	public void setUrlType(String urlType) {
		this.urlType = urlType;
	}

	public String getRoleCode() {
		return roleCode;
	}

	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}

}
