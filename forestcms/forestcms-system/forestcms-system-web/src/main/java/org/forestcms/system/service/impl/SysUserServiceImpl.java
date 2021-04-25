package org.forestcms.system.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.forestcms.common.core.dto.UserDTO;
import org.forestcms.system.entity.SysRole;
import org.forestcms.system.entity.SysUser;
import org.forestcms.system.entity.SysUserRole;
import org.forestcms.system.mapper.SysRoleMapper;
import org.forestcms.system.mapper.SysUserMapper;
import org.forestcms.system.service.SysUserRoleService;
import org.forestcms.system.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.additional.query.impl.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


@Service("sysUserService")
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {
	
	@Autowired
	private SysUserMapper sysUserMapper;
	
	@Autowired
	private SysUserRoleService sysUserRoleService;
	
	@Autowired
	private SysRoleMapper sysRoleMapper;
	
	
	

	@SuppressWarnings("unchecked")
	@Override
	public UserDTO getUserByUserName(String userName) {
		UserDTO UserDTO=null;
		SysUser susUser = new LambdaQueryChainWrapper<>(sysUserMapper).eq(SysUser::getLoginName, userName).one();
		if(susUser!=null) {
			Map<String,Object> map=new HashMap<String, Object>();
			map.put("userId", susUser.getId());
			List<String> role = sysRoleMapper.getUserRole(map).stream().map(SysRole::getRoleCode).collect(Collectors.toList());
			UserDTO=new UserDTO(susUser.getId(),susUser.getLoginName(),susUser.getPassword(),susUser.getIsStatus(),susUser.getName(),role);
		}
		return UserDTO;
	}

	@Override
	public int editPass(Long userId, String password) {
		SysUser user = sysUserMapper.selectById(userId);
		if(user!=null) {
			user.setPassword(password);
			return sysUserMapper.updateById(user);
		}
		return 0;
	}

	@Override
	public int saveUser(SysUser user) {
		user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
		int insert = sysUserMapper.insert(user);
	    if(insert>0) {
	    	String roleIds = user.getRoleIds();
		    if(!StringUtils.isEmpty(roleIds)) {
		        String[] roles = roleIds.split(",");
		        SysUserRole userRole=null;
		        List<SysUserRole> list=new ArrayList<SysUserRole>();
		        for (String roleId : roles) {
		          userRole=new SysUserRole();
		          userRole.setUserId(user.getId());
		          userRole.setRoleId(Long.parseLong(roleId));
		          list.add(userRole);
		        }
		        sysUserRoleService.saveBatch(list);
		    }
	    }
		return insert;
	}

	@Override
	public int updateUser(SysUser user) {
		String roleIds = user.getRoleIds();
	    if(!StringUtils.isEmpty(roleIds)) {
	      QueryWrapper<SysUserRole> wrapper=new QueryWrapper<SysUserRole>();
	      wrapper.eq("user_id", user.getId());
	      sysUserRoleService.remove(wrapper);
	      
	      String[] roles = roleIds.split(",");
	      SysUserRole userRole=null;
	      List<SysUserRole> list=new ArrayList<SysUserRole>();
	      for (String roleId : roles) {
	        userRole=new SysUserRole();
	        userRole.setUserId(user.getId());
	        userRole.setRoleId(Long.parseLong(roleId));
	        list.add(userRole);
	      }
	      sysUserRoleService.saveBatch(list);
	    }
	    int i=sysUserMapper.updateById(user);
	    return i;
	}

	@Override
	public SysUser getByUserId(Long id) {
		SysUser su = sysUserMapper.selectById(id);
		if(su!=null) {
			Map<String, Object> map=new HashMap<String, Object>();
		    map.put("userId",su.getId());
		    List<SysRole> roleByUserId = sysRoleMapper.getUserRole(map);
		    if(!CollectionUtils.isEmpty(roleByUserId)) {
		    	su.setSysRoleList(roleByUserId);
		    }
		}
		return su;
	}

}
