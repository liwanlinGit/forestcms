package org.forestcms.system.web;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import org.forestcms.common.core.result.CommonResult;
import org.forestcms.common.web.util.SysLogs;
import org.forestcms.system.entity.SysDictionaryType;
import org.forestcms.system.service.SysDictionaryTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sys/dictType")
@SysLogs(desc = "字典类别管理")
public class SysDictionaryTypeController {

	@Autowired
	private SysDictionaryTypeService sysDictionaryTypeService;

	@RequestMapping("/list")
	@SysLogs(desc = "字典类别列表")
	public CommonResult list(Long page, Long pageSize, String name) {
		Page<SysDictionaryType> ipage = new Page<SysDictionaryType>(page, pageSize);
		QueryWrapper<SysDictionaryType> queryWrapper = new QueryWrapper<SysDictionaryType>();
		if (!StringUtils.isEmpty(name)) {
			queryWrapper.like("name", name).or().like("code", name);
		}
		queryWrapper.orderByAsc("id");
		IPage<SysDictionaryType> selectPage = sysDictionaryTypeService.page(ipage, queryWrapper);
		return CommonResult.success(selectPage);
	}

	@RequestMapping("/getById")
	public CommonResult getById(Long id) {
		return CommonResult.success(sysDictionaryTypeService.getById(id));
	}

	@RequestMapping("/save")
	@SysLogs(desc = "添加字典类别")
	public CommonResult save(SysDictionaryType sysDictionaryType) {
		boolean save = sysDictionaryTypeService.save(sysDictionaryType);
		if (save) {
			return CommonResult.success();
		}
		return CommonResult.failed();
	}

	@RequestMapping("/update")
	@SysLogs(desc = "修改字典类别")
	public CommonResult update(SysDictionaryType sysDictionaryType) {
		boolean update = sysDictionaryTypeService.updateById(sysDictionaryType);
		if (update) {
			return CommonResult.success();
		}
		return CommonResult.failed();
	}

	@RequestMapping("/delete")
	@SysLogs(desc = "删除字典类别")
	public CommonResult delete(Long id) {
		boolean remove = sysDictionaryTypeService.removeById(id);
		if (remove) {
			return CommonResult.success();
		}
		return CommonResult.failed();

	}

	

}
