<%@include file="../include/common.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <title>吾爱破解-会员注册</title>
    <link rel="icon" type="image/png" href="${basePath}static/common/favicon.png" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="${basePath}static/bootstrap/css/bootstrap.min.css" rel="stylesheet">
    <link href="${basePath}static/theme/${_theme}/global.css" rel="stylesheet">
    <link href="${basePath}static/theme/${_theme}/style.css" rel="stylesheet">
    <link href="${basePath}static/theme/${_theme}/body.css" rel="stylesheet">
    <style type="text/css">
	  #errors{
	  	padding-left: 0;
	  }
    </style>
    <link href="${basePath}static/bootstrap/css/bootstrap-responsive.min.css" rel="stylesheet">

    <!-- HTML5 shim, for IE6-8 support of HTML5 elements -->
    <!--[if lt IE 9]>
      <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->
  </head>

  <body>
	<div class="container">
	<section id="content">
		<form id="login_form" onsubmit="return checkRegister();" action="${basePath}register" method="post">
			<h2>会员注册</h2>
			<div id="errors"></div>
			<div>
				<input type="text" placeholder="用户名" id="username" name="username"/>
			</div>
			<div>
				<input type="text" placeholder="邮箱" id="email" name="email"/>
			</div>
			<div>
				<input type="password" placeholder="密码" id="passwd" name="passwd"/>
			</div>
			<div>
				<input type="password" placeholder="确认密码" id="repasswd" name="repasswd"/>
			</div>
			 <div class="">
				<span class="help-block u-errormessage" id="js-server-helpinfo">&nbsp;</span>			</div> 
			<div>
				<!-- <input type="submit" value="Log in" /> -->
				<input type="submit" value="注册" class="btn btn-primary" id="js-btn-register"/>
				<a href="${basePath}login">已注册账号</a>
			</div>
		</form><!-- form -->
	</section><!-- content -->
</div>
<!-- container -->
<br><br><br><br>
<div style="text-align:center;">
</div>
<script type="text/javascript" src="${basePath}static/jquery/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="${basePath}static/theme/${_theme}/global.js"></script>
<script type="text/javascript">

</script>
  </body>
</html>