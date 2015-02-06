package com.tuziilm.dxj.common;

import java.util.Date;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * @author <a href="tuziilm@gmail.com">tuziilm</a>
 *
 */
public class MailSend {
	//private String host = "smtp.163.com";
		private String host = Config.getParam("host");
		// 这个是你的邮箱用户名
		private String username = Config.getParam("username");
		// 你的邮箱密码
		private String password = Config.getParam("password");

		private String mail_head_name = Config.getParam("mail_head_name");

		private String mail_head_value = Config.getParam("mail_head_value");

		//private String mail_to = SysParam.getParam("mail_to");

		private String mail_from = Config.getParam("mail_from");

		private String mail_subject = Config.getParam("mail_subject");
			
		//private String mail_body = SysParam.getParam("mail_body");

		private String personalName = Config.getParam("personalName");

		public String send(String mail_to,String mail_body) throws Exception {
			String info = "";
			try {
				Properties props = new Properties(); // 获取系统环境
				Authenticator auth = new Email_Autherticator(); // 进行邮件服务器用户认证
				props.put("mail.smtp.host", host);
				props.put("mail.smtp.auth", "true");
				Session session = Session.getDefaultInstance(props, auth);
				// 设置session,和邮件服务器进行通讯。
				MimeMessage message = new MimeMessage(session);
				// message.setContent("foobar, "application/x-foobar"); // 设置邮件格式
				message.setSubject(mail_subject); // 设置邮件主题
				message.setText(mail_body); // 设置邮件正文
				message.setHeader(mail_head_name, mail_head_value); // 设置邮件标题
				message.setSentDate(new Date()); // 设置邮件发送日期
				Address address = new InternetAddress(mail_from, personalName);
				message.setFrom(address); // 设置邮件发送者的地址
				//Address toAddress = new InternetAddress(mail_to); // 设置邮件接收方的地址
				InternetAddress[] toAddress = new InternetAddress().parse(mail_to);
				//message.addRecipient(Message.RecipientType.TO, toAddress);
				message.setRecipients(Message.RecipientType.TO, toAddress);
				Transport.send(message); // 发送邮件
				System.out.println("send ok!");
				info = "success";
			} catch (Exception ex) {
				info = "error";
				ex.printStackTrace();
				throw new Exception(ex.getMessage());
			}
			return info;
		}

		/** */
		/**
		 * 用来进行服务器对用户的认证
		 */
		public class Email_Autherticator extends Authenticator {
			public Email_Autherticator() {
				super();
			}

			public Email_Autherticator(String user, String pwd) {
				super();
				username = user;
				password = pwd;
			}

			public PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		}
		public static void main(String[] args) {
			MailSend ms = new MailSend();
			try {
				ms.send("1462479893@qq.com","sfafd");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
}
