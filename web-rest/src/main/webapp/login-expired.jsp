<%@ page import="org.springframework.security.authentication.CredentialsExpiredException" %>
<%@ page import="org.springframework.security.authentication.BadCredentialsException" %>
<%@ page import="org.springframework.security.authentication.LockedException" %>

<%@ page isErrorPage="true" %>
<%if (request.getAttribute("exception") instanceof CredentialsExpiredException) {%>
    <%=request.getAttribute("client")%>
<%} else if (request.getAttribute("exception") instanceof BadCredentialsException || request.getAttribute("exception") instanceof LockedException) {%>
    <%=request.getAttribute("loginError")%>
<%} else {%>
    <%=request.getAttribute("exception")%>
<%}%>