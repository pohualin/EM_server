<%@ page import="com.emmisolutions.emmimanager.web.rest.admin.security.cas.XhrNeedsRedirectException" %>
<%@ page session="false" %>
<%@ page isErrorPage="true" %>
<%
response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
if (exception instanceof XhrNeedsRedirectException) {%>
{ "url": "<%=((XhrNeedsRedirectException) exception).getUrl()%>"}
<%}%>