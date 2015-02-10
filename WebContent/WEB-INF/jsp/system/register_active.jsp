<%@include file="../include/common.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <title>吾爱破解-会员注册</title>
    <link rel="icon" type="image/png" href="${basePath}static/common/favicon.png"" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="${basePath}static/bootstrap/css/bootstrap.min.css" rel="stylesheet">
    <link href="${basePath}static/theme/${_theme}/global.css" rel="stylesheet">
    <script type="text/javascript" src="${basePath}static/jquery/jquery-1.8.3.min.js"></script>
	<script type="text/javascript" src="${basePath}static/theme/${_theme}/global.js"></script>
	<script type="text/javascript">
			function toLogin(){
					window.location.replace("/");
			}
			function changeNumber(){
					var time = $("#service").html();
					time-=1;
					$("#service").html(time);
					if(time == 0){	
					toLogin();
					}
			}
			$(function(){
					setInterval(changeNumber,1000);
			});
		</script>
	</head>
	<body>
		<c:if test="${flag}">
		<div class="login_step">
			注册步骤: 1.填写信息 &gt; 2.验证邮箱 &gt;
			<span class="red_bold">3.注册成功</span>
		</div>
		<div class="login_success">
			<div class="login_bj">
				<div class="succ">
					<img
						src="static/common/register_success.jpg" />
				</div>
				<h5>
					${user.username}，欢迎加入吾爱破解
				</h5>
				<h6>
					请牢记您的登录邮件地址：${user.email}
				</h6>
				<h3 align="center" style="color:red">
					<a id="djs" href="${basePath}"> <span id="service">5</span>秒后将自动跳转到首页！(立即跳转请点击)
					</a>
				</h3>
			</div>
		</div>
		</c:if>
		<c:if test="${flag==false}">
			<div class="login_step">
				注册步骤: 1.填写信息 &gt; 2.验证邮箱 &gt;
				<span class="red_bold">3.注册失败</span>
			</div>
			<div class="login_success">
				<div class="login_bj">
					<h5 align="center" style="color:red">${info}</h5>
				</div>
			</div>
		</c:if>
	</body>
<c:import url="../theme/${_theme}/footer.jsp"></c:import>

