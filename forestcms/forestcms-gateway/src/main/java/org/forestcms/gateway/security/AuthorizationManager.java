package org.forestcms.gateway.security;

import cn.hutool.core.convert.Convert;

import org.apache.commons.lang3.StringUtils;
import org.forestcms.common.core.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import com.fasterxml.jackson.databind.JsonNode;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.Payload;

import reactor.core.publisher.Mono;

import java.net.URI;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.security.sasl.AuthenticationException;

/**
 * 鉴权管理器，用于判断是否有资源的访问权限
 */
@Component
public class AuthorizationManager implements ReactiveAuthorizationManager<AuthorizationContext> {

	 @Autowired
	 private RedisTemplate redisTemplate;
	    
    @Override
    public Mono<AuthorizationDecision> check(Mono<Authentication> mono, AuthorizationContext authorizationContext) {
        //从Redis中获取当前路径可访问角色列表
        URI uri = authorizationContext.getExchange().getRequest().getURI();
        ServerHttpRequest request = authorizationContext.getExchange().getRequest();
        if (request.getMethod() == HttpMethod.OPTIONS) {
            return Mono.just(new AuthorizationDecision(true));
        }
        String token = request.getHeaders().getFirst("Authorization");
        List<String> authorities=null;
        if(!StringUtils.isAllBlank(token)) {
        	 String realToken = token.replace("Bearer ", "");
        	 Object object = redisTemplate.opsForValue().get(realToken);
        	 if(object==null) {
        		 throw new AccountExpiredException("该账号已过期，请联系管理员!");
        	 }else {
        		 //获取角色
				try {
					JsonNode readTree = JsonUtil.readTree(JWSObject.parse(realToken).getPayload().toString());
					Object obj = redisTemplate.opsForHash().get("AUTH:RESOURCE_ROLES_MAP:"+readTree.get("id").asText(), uri.getPath());
					authorities = Convert.toList(String.class,obj);
					authorities = authorities.stream().map(i -> i = "ROLE_" + i).collect(Collectors.toList());
				} catch (ParseException e) {
					e.printStackTrace();
				}	 
        	 }
        }
        //认证通过且角色匹配的用户可访问当前路径
       
        return mono
                .filter(Authentication::isAuthenticated)
                .flatMapIterable(Authentication::getAuthorities)
                .map(GrantedAuthority::getAuthority)
                .any(authorities::contains)
                .map(AuthorizationDecision::new)
                .defaultIfEmpty(new AuthorizationDecision(false));
    }

}
