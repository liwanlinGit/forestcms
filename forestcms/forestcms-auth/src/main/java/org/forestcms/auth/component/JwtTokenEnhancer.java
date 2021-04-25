package org.forestcms.auth.component;


import org.forestcms.auth.dto.User;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT内容增强器
 */
@Component
public class JwtTokenEnhancer implements TokenEnhancer {
    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        User user = (User) authentication.getUserAuthentication().getPrincipal();
        Map<String, Object> info = new HashMap<>();
        //把用户ID设置到JWT中
        info.put("id", user.getId());
        try {
        	byte[] bytes = user.getRealName().getBytes("UTF-8");
        	String encodeToString = Base64.getEncoder().encodeToString(bytes);
			info.put("realName", encodeToString);
			info.put("password", user.getPassword());
			info.put("roles", user.getAuthorities());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(info);
        return accessToken;
    }
}
