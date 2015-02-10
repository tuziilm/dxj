package com.tuziilm.dxj.mvc;

import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.tuziilm.dxj.common.Config;
import com.tuziilm.dxj.common.DesUtil;
import com.tuziilm.dxj.common.LoginContext;
import com.tuziilm.dxj.common.MailSend;
import com.tuziilm.dxj.common.RequestUtils;
import com.tuziilm.dxj.common.SecurityUtils;
import com.tuziilm.dxj.domain.SysUser;
import com.tuziilm.dxj.service.SysUserService;

/**
 * 登录、登出，系统设置等
 * @author <a href="tuziilm@gmail.com">tuziilm</a>
 *
 */
@Controller
public class SystemController {
	private final Logger log=LoggerFactory.getLogger(getClass());
	private final static MailSend mailSend = new MailSend();
	private final static int MAX_SIZE_CACHE=1000;
	private final static int MAX_TIME_RETRY_PER_IP=50;
	private final static int MAX_TIME_RETRY_PER_IP_AND_ACCOUNT=5;
	private final static long EXPIRE_TIME_IN_MINUTES=10L;
	private final Cache<String, Integer> incorrectAccessCache=CacheBuilder.newBuilder().maximumSize(MAX_SIZE_CACHE).expireAfterWrite(EXPIRE_TIME_IN_MINUTES, TimeUnit.MINUTES).<String, Integer>build();
	
	protected final String REGISTER_SUCCESS;//激活用户成功页面

	public SystemController(){
		REGISTER_SUCCESS=String.format("/%s/register_active", "system");
	}
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
	@RequestMapping(value="/registerSuccess",method=RequestMethod.GET)
	public String registerSuccess(){
  		return "/system/registerSuccess";
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
		return "({\"success\":true,\"msg\":\"登录成功！\"})";
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
		user.setIp(RequestUtils.getRemoteIp(request));
		user.setEmail(email);
		user.setPasswd(SecurityUtils.md5Encode(passwd, username));
		user.setUsername(username);
		user.setSysUserType((byte)3);
		user.setStatus((byte)0);//未激活状态
		user.setPrivilege("3");
		user.setEmailVaildateCode(SecurityUtils.md5Encode(email, Config.SECURITLY_KEY));
		sysUserService.save(user);
		sendEmail(user);
		return "({\"success\":true,\"msg\":\"注册成功！\"})";
	}
	/**
	 * 发送邮件
	 * @author tuziilm
	 * @param user
	 * 2015年2月10日
	 */
	public void sendEmail(SysUser user){
		///邮件的内容
        StringBuffer sb=new StringBuffer("您刚刚注册了最代码，请点击以下链接完成注册：</br>");
        sb.append("<a href=\"http://localhost:8080/registerActive?email=");
        sb.append(DesUtil.encode(user.getEmail())); 
        sb.append("&validateCode="); 
        sb.append(DesUtil.encode(user.getEmailVaildateCode()));
        sb.append("\">http://localhost:8080/registerActive?email="); 
        sb.append(DesUtil.encode(user.getEmail()));
        sb.append("&validateCode=");
        sb.append(DesUtil.encode(user.getEmailVaildateCode()));
        sb.append("</a>");
        System.out.println(sb);
        try {
			mailSend.send(user.getEmail(), sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 注册激活
	 * @author tuziilm
	 * @return
	 * 2015年2月10日
	 */
	@RequestMapping("/registerActive")
	public String registerActive(@RequestParam("email") String email, @RequestParam("validateCode") String validateCode, Model model, HttpSession session, HttpServletRequest request, HttpServletResponse response){
		SysUser user = sysUserService.getByEmail(DesUtil.decode(email));
        //验证用户是否存在   
        if(user!=null) {
        	//验证用户激活状态    
        	if(user.getStatus()==0) {
        		Date now = new Date();
        		System.out.println(now+"$$$$$$$$$$$$$"+DateUtils.addDays(user.getGmtModified(),2)+"=============邮箱验证请在48小时之内完成"+now.after(DateUtils.addDays(user.getGmtModified(),2)));
        		if(!now.after(DateUtils.addDays(user.getGmtModified(),2))){
        			if(DesUtil.decode(validateCode).equals(user.getEmailVaildateCode())) {    
                        //激活成功， //并更新用户的激活状态，为已激活   
                         user.setStatus((byte)1);//把状态改为激活  
                        sysUserService.update(user);
                        LoginContext.doLogin(user, session);
                        model.addAttribute("info", "激活成功！");
                        model.addAttribute("flag", true);
                    } else {    
                    	model.addAttribute("info", "激活失败！");
                    	model.addAttribute("flag", false);
                    }   
        		}else{
        			model.addAttribute("info", "验证码已过期！");
        			model.addAttribute("flag", false);
        		}
        	}else{
        		LoginContext.doLogin(user, session);
        		model.addAttribute("info", "该邮箱已激活！");
        		model.addAttribute("flag", true);
        	}
        }else{
        	model.addAttribute("info", "该邮箱未注册！");
        	model.addAttribute("flag", false);
        }
        model.addAttribute("user", user);
        return REGISTER_SUCCESS;
	}
}
