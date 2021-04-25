package org.forestcms.system.mapper;


import java.util.List;
import java.util.Map;

import org.forestcms.system.entity.SysDictionaryData;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

public interface SysDictionaryDataMapper extends BaseMapper<SysDictionaryData> {
  
  List<SysDictionaryData> selectData(Map<String, Object> map);
}
