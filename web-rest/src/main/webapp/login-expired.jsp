<%@ page import="com.emmisolutions.emmimanager.exception.IpAddressAuthenticationException" %>
<%@ page import="org.springframework.security.authentication.BadCredentialsException" %>
<%@ page import="org.springframework.security.authentication.CredentialsExpiredException" %>
<%@ page import="org.springframework.security.authentication.LockedException" %>
<%@ page session="false" %>
<%@ page isErrorPage="true" %>
<% Object e = request.getAttribute("exception");
    if (e instanceof CredentialsExpiredException
            || e instanceof BadCredentialsException
            || e instanceof LockedException
            || e instanceof IpAddressAuthenticationException) {%>
    <%=request.getAttribute("loginError")%>
<%} else {%>
    <%=request.getAttribute("exception")%>
<%}%>