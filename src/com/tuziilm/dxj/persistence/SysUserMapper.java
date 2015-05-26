package com.tuziilm.dxj.persistence;

import com.tuziilm.dxj.domain.SysUser;

/**
 * ibatis操作系统用户表的Mapper接口
 * @author <a href="tuziilm@163.com">tuziilm</a>
 *
 */
public interface SysUserMapper extends BaseMapper<SysUser>{

	SysUser getByUsername(String username);
	SysUser getByEmail(String email);
}