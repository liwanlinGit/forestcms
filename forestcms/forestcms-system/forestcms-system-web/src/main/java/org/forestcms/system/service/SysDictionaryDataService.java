package org.forestcms.system.service;

import java.util.List;
import java.util.Map;

import org.forestcms.system.entity.SysDictionaryData;

import com.baomidou.mybatisplus.extension.service.IService;

public interface SysDictionaryDataService extends IService<SysDictionaryData> {
	
	public List<SysDictionaryData> selectData(Map<String, Object> map);
}
