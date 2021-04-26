package org.forestcms.auth.services;


import org.apache.commons.lang.StringUtils;
import org.forestcms.auth.dto.User;
import org.forestcms.common.core.dto.UserDTO;
import org.forestcms.system.api.UsersFeignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 用户管理业务类
 */
@Service
public class UserServiceImpl implements UserDetailsService {

    @Autowired
    private UsersFeignService usersFeignService;
    
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private RedisTemplate redisTemplate;

    @PostConstruct
    public void initData() {
        String password = passwordEncoder.encode("123456");
        System.out.println(password);
    }

    @Override
    public UserDetails loadUserByUsername(String username)  {
    	UserDTO userDto  = usersFeignService.getUserByUsername(username);
    	if(userDto==null) {
    		throw new UsernameNotFoundException("用户名或密码错误");
    	}
        User securityUser = new User(userDto);
        securityUser.setEnabled(true);
        if (!securityUser.isEnabled()) {
            throw new DisabledException("该账户已被禁用，请联系管理员!");
        } else if (!securityUser.isAccountNonLocked()) {
            throw new LockedException("该账号已被锁定，请联系管理员!");
        } else if (!securityUser.isAccountNonExpired()) {
            throw new AccountExpiredException("该账号已过期，请联系管理员!");
        } else if (!securityUser.isCredentialsNonExpired()) {
            throw new CredentialsExpiredException("该账户的登录凭证已过期，请重新登录!");
        }
        Map<String, String> roleUrl = usersFeignService.getPermissionUrlByRoleCode(StringUtils.join(userDto.getRoles().toArray()));
        redisTemplate.delete("AUTH:RESOURCE_ROLES_MAP:"+userDto.getId());
        redisTemplate.opsForHash().putAll("AUTH:RESOURCE_ROLES_MAP:"+userDto.getId(), roleUrl);
        return securityUser;
    }
    

}
