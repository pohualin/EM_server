<%@ page import="com.emmisolutions.emmimanager.web.rest.admin.configuration.cas.XhrNeedsRedirectException" %>
<%@ page isErrorPage="true" %>
<%
response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
if (exception instanceof XhrNeedsRedirectException) {%>
{ "url": "<%=((XhrNeedsRedirectException) exception).getUrl()%>"}
<%}%>