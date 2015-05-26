package com.tuziilm.dxj.service;

import com.tuziilm.dxj.domain.SysUser;
import com.tuziilm.dxj.persistence.SysUserMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 系统用户数据操作服务类
 * @author <a href="tuziilm@163.com">tuziilm</a>
 *
 */
@Service
public class SysUserService  extends SimpleCacheSupportService<SysUser> {
	private SysUserMapper sysUserMapper;
	@Autowired
	public void setSysUserMapper(SysUserMapper sysUserMapper) {
		this.mapper = sysUserMapper;
		this.sysUserMapper=sysUserMapper;
	}

	public SysUser getByUsername(String username) {
		return sysUserMapper.getByUsername(username);
	}
	public SysUser getByEmail(String email){
		return sysUserMapper.getByEmail(email);
	}
}
