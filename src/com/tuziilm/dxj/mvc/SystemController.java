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
 * ��¼���ǳ���ϵͳ���õ�
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
	
	protected final String REGISTER_ACTIVE;//�����û��ɹ�ҳ��
	protected final String FIND_PASSWD;//�û��һ��������

	public SystemController(){
		REGISTER_ACTIVE=String.format("/%s/register_active", "system");
		FIND_PASSWD=String.format("/%s/find_passwd", "system");
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
	@RequestMapping(value="/find",method=RequestMethod.GET)
	public String find(){
  		return "/system/find_passwd";
	}
	@RequestMapping(value="/registerSuccess",method=RequestMethod.GET)
	public String registerSuccess(){
  		return "/system/login";
	}
	@RequestMapping(value="/login",method=RequestMethod.POST,produces="application/javascript;charset=UTF-8")
	public @ResponseBody String login(@RequestParam("email") String email, @RequestParam("passwd") String passwd, HttpSession session, HttpServletRequest request, HttpServletResponse response) throws NoSuchAlgorithmException{
		SysUser sysUser = sysUserService.getByEmail(email);
		String ip=RequestUtils.getRemoteIp(request);
		String ipAccount=ip+"-"+email;
		Integer ipRetryCount = incorrectAccessCache.getIfPresent(ip);
		if(ipRetryCount!=null && ipRetryCount>MAX_TIME_RETRY_PER_IP){
			return  "({\"success\":false,\"msg\":\"��IP�Ѿ�������"+EXPIRE_TIME_IN_MINUTES+"���ӣ�\"})";
		}
		Integer ipAccountRetryAccount = incorrectAccessCache.getIfPresent(ipAccount);
		if(ipAccountRetryAccount!=null && ipAccountRetryAccount>MAX_TIME_RETRY_PER_IP_AND_ACCOUNT){
			return  "({\"success\":false,\"msg\":\"���¼��"+email+"�˺��Ѿ�������"+EXPIRE_TIME_IN_MINUTES+"���ӣ�\"})";
		}
		if(sysUser==null || !SecurityUtils.md5Encode(passwd, email).equals(sysUser.getPasswd())){
			incorrectAccessCache.put(ip, ipRetryCount==null?1:ipRetryCount+1);
			incorrectAccessCache.put(ipAccount, ipAccountRetryAccount==null?1:ipAccountRetryAccount+1);
			log.error("{}[{}] login failed!",email,ip);
			return "({\"success\":false,\"msg\":\"�û������ڻ�������ȷ��\"})";
		}
		if(sysUser.getStatus()==0){
			return "({\"success\":false,\"msg\":\"��������δ���\"})";
		}
		LoginContext.doLogin(sysUser, session);
		return "({\"success\":true,\"msg\":\"��¼�ɹ���\"})";
	}
	/**
	 * �����ʼ�
	 * @author tuziilm
	 * @param user
	 * 2015��2��10��
	 */
	public void sendEmail(SysUser user){
		///�ʼ�������
        StringBuffer sb=new StringBuffer("���ո�ע��������룬���������������ע�᣺</br>");
        sb.append("<a href=\"http://localhost:8080/registerActive?email=");
        sb.append(DesUtil.encode(user.getEmail()));
		sb.append("&validateCode=");
        sb.append(user.getEmailVaildateCode());
        sb.append("\">http://localhost:8080/registerActive?email=");
        sb.append(DesUtil.encode(user.getEmail()));
        sb.append("&validateCode=");
        sb.append(DesUtil.encode(user.getEmailVaildateCode()));
        sb.append("</a>");
        try {
			mailSend.send(user.getEmail(), sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void sendPasswd(String email,String passwd){
		StringBuffer sb = new StringBuffer("�����������ǣ�");
		sb.append(passwd).append("��Ո�M���޸��ܴa��");
		try {
			mailSend.send(email, sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public int rand(int min,int max){
		return (int) Math.floor(Math.max(min, Math.random()*(max+1)));
	}
	/**
	 * ע�ἤ��
	 * @author tuziilm
	 * @return
	 * 2015��2��10��
	 */
	@RequestMapping(value="/registerActive",method=RequestMethod.GET)
	public String registerActive(@RequestParam("email") String email, @RequestParam("validateCode") String validateCode, Model model, HttpSession session, HttpServletRequest request, HttpServletResponse response) {
		System.out.println(email+"---------------");
		System.out.println(DesUtil.decode(email));
		SysUser user = sysUserService.getByEmail(DesUtil.decode(email));
		//��֤�û��Ƿ����
		if (user != null) {
			//��֤�û�����״̬
			if (user.getStatus() == 0) {
				Date now = new Date();
				if (!now.after(DateUtils.addDays(user.getGmtModified(), 2))) {
					if (DesUtil.decode(validateCode).equals(user.getEmailVaildateCode())) {
						//����ɹ��� //�������û��ļ���״̬��Ϊ�Ѽ���
						user.setStatus((byte) 1);//��״̬��Ϊ����
						sysUserService.update(user);
						LoginContext.doLogin(user, session);
						model.addAttribute("info", "����ɹ���");
						model.addAttribute("flag", true);
					} else {
						model.addAttribute("info", "����ʧ�ܣ�");
						model.addAttribute("flag", false);
					}
				} else {
					model.addAttribute("info", "��֤���ѹ��ڣ�");
					model.addAttribute("flag", false);
				}
			} else {
				LoginContext.doLogin(user, session);
				model.addAttribute("info", "�������Ѽ��");
				model.addAttribute("flag", true);
			}
		} else {
			model.addAttribute("info", "������δע�ᣡ");
			model.addAttribute("flag", false);
		}
		model.addAttribute("user", user);
		return REGISTER_ACTIVE;
	}
	@RequestMapping(value = "/findPasswd",method=RequestMethod.POST,produces="application/javascript;charset=UTF-8")
	public @ResponseBody String findPasswd(@RequestParam("email") String email, Model model, HttpSession session, HttpServletRequest request, HttpServletResponse response){
		SysUser sysUser = sysUserService.getByEmail(email);
		if(sysUser == null){
			return "({\"success\":false,\"message\":\"���䲻���ڣ�\"})";
		}
		String[] text = {"abdefghijkmnqrtwy","ABDEFGHIJKLMNQRTWY","23456789"};
		int location = rand(8,10);
		String passwd = "";
		for(int i=0; i<location; ++i){
			int loc = rand(0, 2);
			passwd += text[loc].charAt(rand(0, text[loc].length()-1));
		}
		sysUser.setPasswd(SecurityUtils.md5Encode(passwd, email));
		sysUserService.update(sysUser);
		sendPasswd(email, passwd);
		return "({\"success\":true,\"message\":\"�ɹ���\",\"info\":\"�����ʼ���ɹ�\"})";
	}
	@RequestMapping(value="/register",method=RequestMethod.POST,produces="application/javascript;charset=UTF-8")
	public @ResponseBody String register(@RequestParam("username") String username, @RequestParam("passwd") String passwd,@RequestParam("email") String email,@RequestParam("repasswd") String repasswd, HttpSession session, HttpServletRequest request, HttpServletResponse response){
		SysUser sysUser = sysUserService.getByEmail(email);
		if(sysUser != null){
			return "({\"success\":false,\"message\":\"��������ע�ᣡ\"})";
		}
		SysUser sysUser1 = sysUserService.getByUsername(username);
		if(sysUser1 != null){
			return "({\"success\":false,\"message\":\"���û�����ע�ᣡ\"})";
		}
		SysUser user = new SysUser();
		user.setIp(RequestUtils.getRemoteIp(request));
		user.setEmail(email);
		user.setPasswd(SecurityUtils.md5Encode(passwd, email));
		user.setUsername(username);
		user.setSysUserType((byte)3);
		user.setStatus((byte)0);//δ����״̬
		user.setPrivilege("3");
		user.setEmailVaildateCode(SecurityUtils.md5Encode(email, Config.SECURITLY_KEY));
		sysUserService.save(user);
		sendEmail(user);
		return "({\"success\":true,\"message\":\"ע��ɹ�,�뼤���ٵ�½��\"})";
	}
}
