package org.forestcms.common.web;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.forestcms.common.core.dto.UserDTO;
import org.forestcms.common.core.util.JsonUtil;
import org.forestcms.common.core.util.UserUtil;
import org.forestcms.common.web.remote.LogFeignService;
import org.forestcms.common.web.util.SysLogs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;



@Aspect
@Component
public class SystemLogAspect {
  
	@Autowired
	private LogFeignService logFeignService;
	
	ThreadLocal<Long> startTime = new ThreadLocal<>();

  
  
  @Before("@annotation(org.forestcms.common.web.util.SysLogs)")
  public void controllerAspect(JoinPoint joinPoint) throws Exception{
	startTime.set(System.currentTimeMillis());
    HttpServletRequest request = getRequest();
    Map<String, Object> map=new HashMap<String, Object>();
    UserDTO user = UserUtil.getUser(request);
    
    Class<?> targetClass = Class.forName(joinPoint.getTarget().getClass().getName());
    String classDesc=targetClass.getAnnotation(SysLogs.class).desc();
    
    map.put("roleName",user.getRoles().toString());
    map.put("userId", Long.parseLong(user.getId()+""));
    map.put("userName", user.getRealName());
    map.put("loginName", user.getUsername());
    map.put("modelName", classDesc);
    map.put("ip", request.getRemoteAddr());
    map.put("url", request.getRequestURI());
    map.put("method", joinPoint.getSignature().getName());
    map.put("methodType", request.getMethod());
    map.put("description", getControllerMethodDescription(joinPoint,targetClass, joinPoint.getSignature().getName()));
    map.put("args", Arrays.toString(joinPoint.getArgs()));
    logFeignService.addLogs(map);
  }

  @AfterReturning(returning = "ret", value="@annotation(org.forestcms.common.web.util.SysLogs)")
  public void doAfterReturning(Object ret) throws Exception {
	  System.out.println("RESPONSE : " + JsonUtil.toJson(ret));
	  System.out.println("SPEND TIME : " + (System.currentTimeMillis() - startTime.get()));
	  
  }
  
  @AfterThrowing(value="@annotation(org.forestcms.common.web.util.SysLogs)",throwing = "e")
  public void AfterThrowing(JoinPoint joinPoint,Throwable e)  throws Exception{
    HttpServletRequest request = getRequest();
    Map<String, Object> map=new HashMap<String, Object>();
    UserDTO user = UserUtil.getUser(request);
    
    Class<?> targetClass = Class.forName(joinPoint.getTarget().getClass().getName());
    String classDesc=targetClass.getAnnotation(SysLogs.class).desc();
    
    map.put("roleName",user.getRoles().toString());
    map.put("userId", Long.parseLong(user.getId()+""));
    map.put("userName", user.getRealName());
    map.put("loginName", user.getUsername());
    map.put("modelName", classDesc);
    map.put("ip", request.getRemoteAddr());
    map.put("url", request.getRequestURI());
    map.put("method", joinPoint.getSignature().getName());
    map.put("methodType", request.getMethod());
    map.put("description", JsonUtil.toJson(e));
    map.put("args", Arrays.toString(joinPoint.getArgs()));
    logFeignService.addExceptionLogs(map);
  }

  

  private HttpServletRequest getRequest() {
    ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    return attributes.getRequest();
  }

 
  
  public static String getControllerMethodDescription(JoinPoint joinPoint,Class<?> targetClass,String methodName) throws Exception {
    Method[] methods = targetClass.getMethods();
    String description = "";
    for (Method method:methods) {
        if (method.getName().equals(methodName)){
            description = method.getAnnotation(SysLogs.class).desc();
            break;
        }
    }
    return description;
  }
  
  
  
}
