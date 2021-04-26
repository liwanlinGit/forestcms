
package org.forestcms.generator.controller;

import org.apache.commons.io.IOUtils;
import org.forestcms.common.core.result.CommonResult;
import org.forestcms.generator.service.SysGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 代码生成器
 *
 */
@Controller
@RequestMapping("/generator")
public class SysGeneratorController {
    @Autowired
    private SysGeneratorService sysGeneratorService;

    /**
     * 列表
     */
    @ResponseBody
    @RequestMapping("/list")
    public CommonResult list(int page,int pageSize,String tableName) {
        Map<String, Object> param=new HashMap<String, Object>();
        param.put("tableName", tableName);
        return CommonResult.success(sysGeneratorService.queryList(param,page,pageSize));
    }

    /**
     * 生成代码
     */
    @RequestMapping("/code")
    public void code(String tables, String moduleName, String packageName, String author, HttpServletResponse response) throws IOException {
        byte[] data = sysGeneratorService.generatorCode(tables.split(","), moduleName, packageName, author);

        response.reset();
        response.setHeader("Content-Disposition", "attachment; filename=\"generator-code.zip\"");
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("application/octet-stream; charset=UTF-8");

        IOUtils.write(data, response.getOutputStream());
    }
}
