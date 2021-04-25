package org.forestcms.system.service.impl;

import org.forestcms.system.entity.SysRolePermissions;
import org.forestcms.system.mapper.SysRolePermissionsMapper;
import org.forestcms.system.service.SysRolePermissionsService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

@Service("sysRolePermissionsService")
public class SysRolePermissionsServiceImpl extends ServiceImpl<SysRolePermissionsMapper, SysRolePermissions> implements SysRolePermissionsService {

}
