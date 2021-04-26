
package org.forestcms.generator.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import org.apache.commons.io.IOUtils;
import org.forestcms.generator.dao.MySQLGeneratorDao;
import org.forestcms.generator.utils.GenUtils;
import org.forestcms.generator.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipOutputStream;

/**
 * 代码生成器
 *
 */
@Service
public class SysGeneratorService {
    @Autowired
    private MySQLGeneratorDao generatorDao;

    public PageInfo queryList(Map<String,Object> map,Integer pages,Integer pageSize) {
        Page<?> page = PageHelper.startPage(pages, pageSize);
        List<Map<String, Object>> list = generatorDao.queryList(map);
        return new PageInfo<>(list);
    }

    public Map<String, String> queryTable(String tableName) {
        return generatorDao.queryTable(tableName);
    }

    public List<Map<String, String>> queryColumns(String tableName) {
        return generatorDao.queryColumns(tableName);
    }

    public byte[] generatorCode(String[] tableNames, String moduleName, String packageName, String author) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);

        for (String tableName : tableNames) {
            //查询表信息
            Map<String, String> table = queryTable(tableName);
            //查询列信息
            List<Map<String, String>> columns = queryColumns(tableName);
            //生成代码
            GenUtils.generatorCode(table, moduleName, packageName, author, columns, zip);
        }
        IOUtils.closeQuietly(zip);
        return outputStream.toByteArray();
    }
}
