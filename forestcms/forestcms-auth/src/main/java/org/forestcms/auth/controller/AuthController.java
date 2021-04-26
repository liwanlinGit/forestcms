package org.forestcms.auth.controller;

import org.bouncycastle.jce.PrincipalUtil;
import org.forestcms.common.core.dto.UserDTO;
import org.forestcms.common.core.result.CommonResult;
import org.forestcms.common.core.util.DateUtil;
import org.forestcms.common.core.util.UserUtil;
import org.forestcms.system.api.UsersFeignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

/**
 * 自定义Oauth2获取令牌接口
 */
@RestController
@RequestMapping("/oauth")
public class AuthController {

    @Autowired
    private TokenEndpoint tokenEndpoint;
    
    @Autowired
    private UsersFeignService usersFeignService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * Oauth2登录认证
     * username
     * password
     * grant_type:password
     * client_id:client-app
     * client_secret:123456
     */
    @RequestMapping(value = "/token", method = RequestMethod.POST)
    public CommonResult<OAuth2AccessToken> postAccessToken(Principal principal, @RequestParam Map<String, String> parameters) throws HttpRequestMethodNotSupportedException {
        OAuth2AccessToken oAuth2AccessToken = tokenEndpoint.postAccessToken(principal, parameters).getBody();
        Map<String, Object> additionalInformation = oAuth2AccessToken.getAdditionalInformation();
        System.out.println(oAuth2AccessToken.getExpiration()+"-=-="+DateUtil.parseDateToStr(oAuth2AccessToken.getExpiration(), "YYYY-MM-dd HH:mm:ss"));
        redisTemplate.opsForValue().set(oAuth2AccessToken.getValue(), additionalInformation.get("roles"),60*60*23,TimeUnit.SECONDS);
        return CommonResult.success(oAuth2AccessToken);
    }
    
    @RequestMapping(value = "/user")
    public CommonResult user(HttpServletRequest request)  {
    	 UserDTO user = UserUtil.getUser(request);
         return CommonResult.success(user.getRealName());
    }
    
    @RequestMapping(value = "/editPass")
    public CommonResult editPass(HttpServletRequest request,String oldPass,String newPass)  {
    	 UserDTO user = UserUtil.getUser(request);
    	 newPass= passwordEncoder.encode(newPass);
    	 if(!passwordEncoder.matches(oldPass, user.getPassword())) {
    		 return CommonResult.failed("原始密码不正确！");
    	 }else {
    		 int editPass = usersFeignService.editPass(user.getId(), newPass);
    		 if(editPass>0) {
    			 return CommonResult.success("密码修改成功！");
    		 }
    	 }
    	 return CommonResult.failed("密码修改失败！");
    	
    }
}
