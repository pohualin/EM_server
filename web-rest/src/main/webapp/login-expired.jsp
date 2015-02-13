<%@ page import="org.springframework.security.authentication.CredentialsExpiredException" %>
<%@ page isErrorPage="true" %>
<%if (request.getAttribute("exception") instanceof CredentialsExpiredException) {%>
    <%=request.getAttribute("client")%>
<%} else {%>
    <%=request.getAttribute("exception")%>
<%}%>