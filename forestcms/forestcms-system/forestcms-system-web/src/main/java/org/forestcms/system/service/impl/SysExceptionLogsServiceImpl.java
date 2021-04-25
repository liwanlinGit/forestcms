package org.forestcms.system.service.impl;

import org.forestcms.system.entity.SysExceptionLogs;
import org.forestcms.system.mapper.SysExceptionLogsMapper;
import org.forestcms.system.service.SysExceptionLogsService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

@Service("sysExceptionLogsService")
public class SysExceptionLogsServiceImpl extends ServiceImpl<SysExceptionLogsMapper, SysExceptionLogs> implements SysExceptionLogsService {

}
