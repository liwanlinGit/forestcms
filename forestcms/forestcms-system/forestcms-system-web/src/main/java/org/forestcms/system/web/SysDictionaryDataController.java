package org.forestcms.system.web;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.HashMap;
import java.util.Map;

import org.forestcms.common.core.result.CommonResult;
import org.forestcms.common.web.util.SysLogs;
import org.forestcms.system.entity.SysDictionaryData;
import org.forestcms.system.entity.SysDictionaryType;
import org.forestcms.system.service.SysDictionaryDataService;
import org.forestcms.system.service.SysDictionaryTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/sys/dictData")
@SysLogs(desc = "字典数据")
public class SysDictionaryDataController {

    @Autowired
    private SysDictionaryDataService  sysDictionaryDataService;

    @Autowired
	private SysDictionaryTypeService sysDictionaryTypeService;
    
    @RequestMapping("/selectData")
    public CommonResult selectData(String typeCode,String dataCode) {
       Map<String, Object> map=new HashMap<String, Object>();
       map.put("typeCode", typeCode);
       map.put("dataCode",dataCode);
       return CommonResult.success(sysDictionaryDataService.selectData(map));
    }
    
    /**
     * 条件分页查询
     *
     * @param page
     * @param pageSize
     * @param type     类型
     * @return
     */
    @RequestMapping("/list")
    @SysLogs(desc = "字典数据列表")
    public CommonResult list(Long page, Long pageSize, Long type, String name) {
        Page<SysDictionaryData> pages = new Page<SysDictionaryData>(page, pageSize);
        QueryWrapper<SysDictionaryData> queryWrapper = new QueryWrapper<SysDictionaryData>();
        queryWrapper.eq("type", type);
        if(!StringUtils.isEmpty(name)) {
          queryWrapper.like("name", name).or().like("code", name);
        }
        queryWrapper.orderByAsc("id");
        IPage<SysDictionaryData> selectPage = sysDictionaryDataService.page(pages, queryWrapper);
        return CommonResult.success(selectPage);
         
    }

    @RequestMapping("/getById")
    public CommonResult getById( Long id) {
        return CommonResult.success(sysDictionaryDataService.getById(id));
    }

    @RequestMapping("/save")
    @SysLogs(desc = "添加字典数据")
    public CommonResult save(SysDictionaryData sysDictionaryData) {
    	boolean save = sysDictionaryDataService.save(sysDictionaryData);
    	if(save) {
    		return CommonResult.success();
    	}
        return CommonResult.failed();
    }

    @RequestMapping("/update")
    @SysLogs(desc = "修改字典数据")
    public CommonResult update( SysDictionaryData sysDictionaryData) {
    	boolean update = sysDictionaryDataService.updateById(sysDictionaryData);
    	if(update) {
    		return CommonResult.success();
    	}
        return CommonResult.failed();
      
    }

    @RequestMapping("/delete")
    @SysLogs(desc = "删除字典数据")
    public CommonResult delete(Long id) {
    	boolean remove = sysDictionaryDataService.removeById(id);
    	if(remove) {
    		return CommonResult.success();
    	}
        return CommonResult.failed();
    }

    @RequestMapping("/getDataType")
	public CommonResult getAll() {
		QueryWrapper<SysDictionaryType> queryWrapper = new QueryWrapper<SysDictionaryType>();
		queryWrapper.orderByAsc("id");
		return CommonResult.success(sysDictionaryTypeService.list(queryWrapper));
	}
}
