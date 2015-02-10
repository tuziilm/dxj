package com.tuziilm.dxj.domain;

import com.tuziilm.dxj.common.SystemUserType;


/**
 * ϵͳ�û���
 * @author <a href="tuziilm@gmail.com">tuziilm</a>
 *
 */
public class SysUser  extends RemarkId {
	/** �ʼ�*/
	private String email;
	/** �ʼ���֤��*/
	private String emailVaildateCode;
    /** �˺�*/
    private String username;
    /** ����*/
    private String passwd;
    /** ϵͳ�û�����,0:ϵͳ����Ա,1:ҵ��Ա, 2:������, 3���ͻ�*/
    private Byte sysUserType;
    /** ״̬,1:����,0:δ����*/
    private Byte status;
    /** Ȩ��ֵ�б�,���磺1|2|3*/
    private String privilege;
    /** ip*/
    private String ip;

    public SystemUserType getSystemUserType(){
    	return SystemUserType.valueOf(sysUserType);
    }
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd == null ? null : passwd.trim();
    }

    public Byte getSysUserType() {
        return sysUserType;
    }

    public void setSysUserType(Byte sysUserType) {
        this.sysUserType = sysUserType;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public String getPrivilege() {
        return privilege;
    }

    public void setPrivilege(String privilege) {
        this.privilege = privilege == null ? null : privilege.trim();
    }

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmailVaildateCode() {
		return emailVaildateCode;
	}

	public void setEmailVaildateCode(String emailVaildateCode) {
		this.emailVaildateCode = emailVaildateCode;
	}
	
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
}