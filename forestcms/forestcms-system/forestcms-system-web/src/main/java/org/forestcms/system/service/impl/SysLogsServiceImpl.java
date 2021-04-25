package org.forestcms.system.service.impl;

import org.forestcms.system.entity.SysLogs;
import org.forestcms.system.mapper.SysLogsMapper;
import org.forestcms.system.service.SysLogsService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


@Service("sysLogsService")
public class SysLogsServiceImpl extends ServiceImpl<SysLogsMapper, SysLogs> implements SysLogsService {

}
