<%@page import="org.joda.time.PeriodType"%>
<%@page import="org.joda.time.Period"%>
<%@page import="org.joda.time.DateTimeZone"%>
<%@page import="org.joda.time.LocalDateTime"%>
<%@page import="com.emmisolutions.emmimanager.model.user.client.UserClient"%>
<%@page import="com.emmisolutions.emmimanager.model.configuration.ClientPasswordConfiguration"%>
<%@ page import="org.springframework.security.authentication.CredentialsExpiredException" %>
<%@ page import="org.springframework.security.authentication.BadCredentialsException" %>
<%@ page import="org.springframework.security.authentication.LockedException" %>

<%@ page isErrorPage="true" %>
<%if (request.getAttribute("exception") instanceof CredentialsExpiredException) {%>
    <%=request.getAttribute("client")%>
<%} else if (request.getAttribute("exception") instanceof BadCredentialsException || request.getAttribute("exception") instanceof LockedException) {
    if(request.getAttribute("userClient") != null && ((UserClient)request.getAttribute("userClient")).isAccountNonLocked()){%>
    Please check your username and password and try again.
  <%} else {
        if(request.getAttribute("policy") != null && ((ClientPasswordConfiguration)request.getAttribute("policy")).getLockoutReset() == 0){%>
            To continue, please contact Emmi Support at 866-294-3664 or support@emmisolutions.com.            
  <%    } else {
              LocalDateTime now = LocalDateTime.now(DateTimeZone.UTC);
              Period diff = new Period(now, ((UserClient)request.getAttribute("userClient")).getLockExpirationDateTime(), PeriodType.minutes());
  %>
            Your account has been temporarily locked due to too many login attempts. You can try again in <%=diff.getMinutes()+1%> minutes, or contact Emmi Support at 866-294-3664 or support@emmisolutions.com.
  <%    }
    }%>
<%}%>