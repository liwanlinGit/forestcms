package org.forestcms.system.service.impl;

import org.forestcms.system.entity.SysLoginLogs;
import org.forestcms.system.mapper.SysLoginLogsMapper;
import org.forestcms.system.service.SysLoginLogsService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


@Service("sysLoginLogsService")
public class SysLoginLogsServiceImpl extends ServiceImpl<SysLoginLogsMapper, SysLoginLogs> implements SysLoginLogsService  {

}
