package org.forestcms.system.service;

import org.forestcms.common.core.dto.UserDTO;
import org.forestcms.system.entity.SysUser;

import com.baomidou.mybatisplus.extension.service.IService;


public interface SysUserService extends IService<SysUser> {


	
	public UserDTO getUserByUserName(String userName);
	
	public int editPass(Long userId,String password);
	
    public int saveUser(SysUser user);
    
    public int updateUser(SysUser user);
    
    public SysUser getByUserId(Long id);
    
  
	
}
