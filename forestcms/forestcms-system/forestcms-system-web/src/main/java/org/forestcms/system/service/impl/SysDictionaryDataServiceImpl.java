package org.forestcms.system.service.impl;

import java.util.List;
import java.util.Map;

import org.forestcms.system.entity.SysDictionaryData;
import org.forestcms.system.mapper.SysDictionaryDataMapper;
import org.forestcms.system.service.SysDictionaryDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

@Service("sysDictionaryDataService")
public class SysDictionaryDataServiceImpl extends ServiceImpl<SysDictionaryDataMapper, SysDictionaryData> implements SysDictionaryDataService {

	@Autowired
	private SysDictionaryDataMapper sysDictionaryDataMapper;
	
	@Override
	public List<SysDictionaryData> selectData(Map<String, Object> map) {
		return sysDictionaryDataMapper.selectData(map);
	}

}
