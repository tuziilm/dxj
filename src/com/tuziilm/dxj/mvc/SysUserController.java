package com.tuziilm.dxj.mvc;

import com.tuziilm.dxj.common.LoginContext;
import com.tuziilm.dxj.common.Paginator;
import com.tuziilm.dxj.common.RemarkForm;
import com.tuziilm.dxj.common.SecurityUtils;
import com.tuziilm.dxj.common.SystemUserType;
import com.tuziilm.dxj.common.Query.NameQuery;
import com.tuziilm.dxj.domain.SysUser;
import com.tuziilm.dxj.service.SysUserService;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DirectFieldBindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import java.security.NoSuchAlgorithmException;

/**
 * 系统用户操作入口
 * @author <a href="pangkunyi@gmail.com">Calvin Pang</a>
 *
 */
@Controller
@RequestMapping("/sysuser")
public class SysUserController extends CRUDController<SysUser, SysUserService, com.tuziilm.dxj.mvc.SysUserController.SysUserForm, NameQuery>{
	public SysUserController() {
		super("sysuser");
	}
	@Resource
	public void setSysUserService(SysUserService sysUserService){
		this.service=sysUserService;
	}
	
	@RequestMapping("/index")
	public String index(){
		if(LoginContext.isAdmin()){
			return REDIRECT_LIST_PAGE;
		}else{
			return "redirect:/sysuser/info_modify/"+LoginContext.getUid();
		}
	}
	/* (non-Javadoc)
	 * @see com.tuziilm.dxj.mvc.ListController#preList(int, com.tuziilm.dxj.common.Paginator, com.tuziilm.dxj.common.Query, org.springframework.ui.Model)
	 */
	@Override
	protected boolean preList(int page, Paginator paginator, NameQuery query,
			Model model) {
		paginator.setNeedTotal(true);
		return super.preList(page, paginator, query, model);
	}
	@RequestMapping(value="/info_save",method=RequestMethod.POST)
	public String infoSave(@Valid SysUserForm form,BindingResult errors, Model model) throws NoSuchAlgorithmException{
		model.addAttribute("errors", errors);
		model.addAttribute("form", form);
		model.addAttribute("isUnderUserInfo", true);
		if(!errors.hasErrors()){
			SysUser sysUser=form.toObj();
			if( sysUser.getId()==null || sysUser.getId()<=0 || sysUser.getId().intValue()!=LoginContext.getUid().intValue()){
				errors.addError(new ObjectError("false", "没有权限修改！"));
			}else{
				//检测密码的正确性
				String passwd = form.getPasswd();
				if(passwd!=null &&passwd.length()>0 && (passwd.length()<4 || passwd.length()>20)){
					errors.addError(new ObjectError("passwd", "密码应为4~20个字符！"));
					model.addAttribute("errors", errors);
					return CREATE_PAGE;
				}
				service.update(sysUser);
				//修改成功,不跳转用户列表
				errors.addError(new ObjectError("success", "修改成功！"));
			}
		}
		return CREATE_PAGE;
	}
	

	@Override
	public void innerSave(SysUserForm form, BindingResult errors, Model model,
			HttpServletRequest request, HttpServletResponse response) {
		SysUser sysUser=form.toObj();
		//检测密码的正确性
		String passwd = form.getPasswd();
		boolean isModify=sysUser.getId()!=null && sysUser.getId()>0;
		if(passwd==null||passwd.length()<1){
				if(!isModify){
				errors.addError(new ObjectError("passwd", "密码不能为空！"));
				model.addAttribute("errors", errors);
				return;
			}
		}else if(passwd.length()<4 || passwd.length()>20){
			errors.addError(new ObjectError("passwd", "密码应为4~20个字符！"));
			model.addAttribute("errors", errors);
			return;
		}
		//执行更新或保存的操作
		try{
			if(isModify){
				service.update(sysUser);
			}else{
				sysUser.setStatus((byte)1);
				sysUser.setPrivilege("1");
				service.save(sysUser);
			}
		}catch(DuplicateKeyException e){
			errors.addError(new ObjectError("database", "名称已经存在！"));
			model.addAttribute("errors", errors);
		}
	}
	
	@RequestMapping("/info_modify/{id}")
	public String infoModify(@PathVariable("id") int id,Model model){
		model.addAttribute("isUnderUserInfo", true);
		if(!LoginContext.isAdmin()&&LoginContext.getUid().intValue()!=id){
			DirectFieldBindingResult errors=new DirectFieldBindingResult("id","id");
			errors.addError(new ObjectError("false", "没有权限修改！"));
			model.addAttribute("errors", errors);
			return CREATE_PAGE;
		}
		SysUser sysUser = service.get(id);
		model.addAttribute("form", sysUser);
		return CREATE_PAGE;
	}
	
	public static class SysUserForm extends RemarkForm<SysUser>{
		@NotNull(message="用户名不能为空")
		@Size(min=1,max=20,message="用户名长度应该在1-20个字符")
		private String username;
		private String passwd;
		private Byte sysUserType;

        @Override
        public void populateObj(SysUser sysUser) {
            sysUser.setUsername(username);
            SystemUserType sut = SystemUserType.valueOf(sysUserType);
            if(LoginContext.isAdmin()&&sut!=SystemUserType.UNKNOWN){
                sysUser.setSysUserType(sysUserType);
            }
            String email = LoginContext.getEmail();
            if(passwd!=null&&passwd.length()>0){
                sysUser.setPasswd(SecurityUtils.md5Encode(passwd, email));
            }
        }

        public Byte getSysUserType() {
			return sysUserType;
		}
		
		public void setSysUserType(Byte sysUserType) {
			this.sysUserType = sysUserType;
		}

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public String getPasswd() {
			return passwd;
		}

		public void setPasswd(String passwd) {
			this.passwd = passwd==null?null:passwd.trim();
		}

        @Override
        public SysUser newObj() {
            return new SysUser();
        }
    }
}
