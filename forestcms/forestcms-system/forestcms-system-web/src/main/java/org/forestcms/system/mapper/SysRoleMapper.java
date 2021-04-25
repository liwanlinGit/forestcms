package org.forestcms.system.mapper;

import java.util.List;
import java.util.Map;

import org.forestcms.system.entity.SysRole;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;


public interface SysRoleMapper extends BaseMapper<SysRole> {

  
  List<SysRole> getRoleByUserId(Map<String, Object> map);
  
  List<SysRole> getUserRole(Map<String, Object> map);
  
}
