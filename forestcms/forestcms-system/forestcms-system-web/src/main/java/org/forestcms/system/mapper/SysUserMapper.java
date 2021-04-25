package org.forestcms.system.mapper;

import java.util.List;
import java.util.Map;

import org.forestcms.system.entity.SysUser;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;


public interface SysUserMapper extends BaseMapper<SysUser> {
  
  List<SysUser> getUserByOrgId(Map<String, Object> map);
}
