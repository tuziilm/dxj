<%@include file="../../include/common.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<c:set var="_pageTitle" value="rom包管理" scope="request"/>
<c:set var="_underRom" value="active" scope="request"/>
<c:set var="_activeRom" value="active" scope="request"/>
<c:set var="_module" value="rom" scope="request"/>
<c:import url="../../theme/${_theme}/header.jsp"></c:import>
<link rel="stylesheet" href="${basePath}static/jquery/jquery-ui.css" />
<!-- main content -->
		<div class="page-header"><h1>rom包管理</h1></div>
		<div id="pageContent">
			<c:import url="../../theme/${_theme}/errors.jsp"></c:import>
			<form action="${basePath}browser/rule/save" method="post" class="form-horizontal">
				<input name="id" type="hidden" value="${form.id}">
				<input name="_queryString" type="hidden" value="${param.queryString}">
                <div class="control-group required-field">
                    <label class="control-label">名称:</label>
                    <div class="controls">
                        <input name="name" value="${fn:escapeXml(form.name)}" type="text" class="input-large">
                    </div>
                </div>
                <div class="control-group required-field">
                    <label class="control-label">链接地址:</label>
                    <div class="controls">
                        <input name="address" value="${fn:escapeXml(form.address)}" type="text" class="input-large">
                    </div>
                </div>
                <div id="_cg_mtitle" class="control-group required-field">
                    <label class="control-label">密码:</label>
                    <div class="controls">
                        <input name="password" value="${fn:escapeXml(form.password)}" type="text" class="input-large">
                    </div>
                </div>
				<div class="control-group required-field">
				  <label class="control-label">备注:</label>
				  <div class="controls">
				    <input name="remark" value="${fn:escapeXml(form.remark)}" type="text" class="input-large">
				  </div>
				</div>
				<div class="form-actions">
				  <input class="btn btn-primary" type="submit" value="保存">
				  <button type="button" class="btn" onclick="javascript:history.go(-1)">取消</button>
				</div>
			</form>
        </div>
<!-- end main content -->
<c:import url="../../theme/${_theme}/footer.jsp"></c:import>
