<%@ page import="com.google.common.base.Strings" %>
<%@ page import="com.zhanghui.rommer.common.Config" %>
<%@ page import="com.google.common.base.Joiner" %>
<%@ page contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" %>
<%@include file="/WEB-INF/jsp/theme/default/ctrl_header.jsp"%>
<%
    String imsi = request.getParameter("imsi");
    if(!Strings.isNullOrEmpty(imsi)){
        Config.TEST_IDENTITIES.add(imsi);
    }
    out.print(Joiner.on(",").join(Config.TEST_IDENTITIES));
%>
<!-- end main body -->
<%@include file="/WEB-INF/jsp/theme/default/ctrl_footer.jsp"%>

