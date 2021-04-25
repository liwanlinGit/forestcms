package org.forestcms.common.mybatis;

import java.util.Date;

import org.apache.ibatis.reflection.MetaObject;
import org.forestcms.common.core.util.DateUtil;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;


@Component
public class MetaObjectHandlerConfig implements MetaObjectHandler {

  @Override
  public void insertFill(MetaObject metaObject) {
    Object createTime = getFieldValByName("createTime", metaObject);
    if (createTime == null) {
      setFieldValByName("createTime",
          DateUtil.parseDateToStr(new Date(), DateUtil.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS), metaObject);// mybatis-plus版本2.0.9+
    }
  }

  @Override
  public void updateFill(MetaObject metaObject) {
    Object updateTime = getFieldValByName("updateTime", metaObject);
    if (StringUtils.isEmpty(updateTime)) {
      setFieldValByName("updateTime",
          DateUtil.parseDateToStr(new Date(), DateUtil.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS), metaObject);// mybatis-plus版本2.0.9+
    }
  }

}
