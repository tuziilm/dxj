package com.tuziilm.dxj.mvc;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.tuziilm.dxj.common.LoginContext;
import com.tuziilm.dxj.common.RequestUtils;
import com.tuziilm.dxj.common.SecurityUtils;
import com.tuziilm.dxj.domain.SysUser;
import com.tuziilm.dxj.service.SysUserService;

import org.apache.catalina.util.MD5Encoder;
import org.apache.commons.codec.digest.Md5Crypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

/**
 * 登录、登出，系统设置等
 * @author <a href="pangkunyi@gmail.com">Calvin Pang</a>
 *
 */
@Controller
public class SystemController {
	private final Logger log=LoggerFactory.getLogger(getClass());
	private final static int MAX_SIZE_CACHE=1000;
	private final static int MAX_TIME_RETRY_PER_IP=50;
	private final static int MAX_TIME_RETRY_PER_IP_AND_ACCOUNT=5;
	private final static long EXPIRE_TIME_IN_MINUTES=10L;
	private final Cache<String, Integer> incorrectAccessCache=CacheBuilder.newBuilder().maximumSize(MAX_SIZE_CACHE).expireAfterWrite(EXPIRE_TIME_IN_MINUTES, TimeUnit.MINUTES).<String, Integer>build();
	@Resource
	private SysUserService sysUserService;
	
	@RequestMapping("/logout")
	public String logout(HttpSession session){
		session.invalidate();
		return "redirect:/login";
	}
	@RequestMapping(value="/login",method=RequestMethod.GET)
	public String login(){
 		return "/system/login";
	}
	@RequestMapping(value="/register",method=RequestMethod.GET)
	public String register(){
  		return "/system/register";
	}
	@RequestMapping(value="/login",method=RequestMethod.POST,produces="application/javascript;charset=UTF-8")
	public @ResponseBody String login(@RequestParam("username") String username, @RequestParam("passwd") String passwd, HttpSession session, HttpServletRequest request, HttpServletResponse response) throws NoSuchAlgorithmException{
		SysUser sysUser = sysUserService.getByUsername(username);
		String ip=RequestUtils.getRemoteIp(request);
		String ipAccount=ip+"-"+username;
		Integer ipRetryCount = incorrectAccessCache.getIfPresent(ip);
		if(ipRetryCount!=null && ipRetryCount>MAX_TIME_RETRY_PER_IP){
			return  "({\"success\":false,\"msg\":\"该IP已经被禁用"+EXPIRE_TIME_IN_MINUTES+"分钟！\"})";
		}
		Integer ipAccountRetryAccount = incorrectAccessCache.getIfPresent(ipAccount);
		if(ipAccountRetryAccount!=null && ipAccountRetryAccount>MAX_TIME_RETRY_PER_IP_AND_ACCOUNT){
			return  "({\"success\":false,\"msg\":\"你登录的"+username+"账号已经被禁用"+EXPIRE_TIME_IN_MINUTES+"分钟！\"})";
		}
		if(sysUser==null || !SecurityUtils.md5Encode(passwd, username).equals(sysUser.getPasswd())){
			incorrectAccessCache.put(ip, ipRetryCount==null?1:ipRetryCount+1);
			incorrectAccessCache.put(ipAccount, ipAccountRetryAccount==null?1:ipAccountRetryAccount+1);
			log.error("{}[{}] login failed!",username,ip);
			return "({\"success\":false,\"msg\":\"用户不存在或密码不正确！\"})";
		}
		LoginContext.doLogin(sysUser, session);
		return "({\"success\":false,\"msg\":\"登录成功！\"})";
	}
	@RequestMapping(value="/register",method=RequestMethod.POST,produces="application/javascript;charset=UTF-8")
	public @ResponseBody String register(@RequestParam("username") String username, @RequestParam("passwd") String passwd,@RequestParam("email") String email,@RequestParam("repasswd") String repasswd, HttpSession session, HttpServletRequest request, HttpServletResponse response){
		SysUser sysUser = sysUserService.getByEmail(email);
		if(sysUser != null){
			return "({\"success\":false,\"msg\":\"该邮箱已注册！\"})";
		}
		SysUser sysUser1 = sysUserService.getByUsername(username);
		if(sysUser1 != null){
			return "({\"success\":false,\"msg\":\"该用户名已注册！\"})";
		}
		SysUser user = new SysUser();
		user.setEmail(email);
		user.setPasswd(passwd);
		user.setUsername(username);
		user.setSysUserType((byte)3);
		user.setStatus((byte)0);
		user.setPrivilege("3");
		user.setEmailVaildateCode(SecurityUtils.md5Encode(email, "5adxj"));
//		sysUserService.save(user);
		///邮件的内容
        StringBuffer sb=new StringBuffer("您刚刚注册了最代码，请点击以下链接完成注册：</br>");
        sb.append("<a href=\"http://localhost:8080/checkEmail?email=");
        sb.append(email); 
        sb.append("&validateCode="); 
        sb.append(user.getEmailVaildateCode());
        sb.append("\">http://localhost:8080/checkEmail?email="); 
        sb.append(email);
        sb.append("&validateCode=");
        sb.append(user.getEmailVaildateCode());
        sb.append("</a>");
        System.out.println(sb);
		return "({\"success\":true,\"msg\":\"注册成功！\"})";
	}
}
