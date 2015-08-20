package com.emmisolutions.emmimanager.service.spring.audit;

import com.emmisolutions.emmimanager.model.audit.login.Login;
import com.emmisolutions.emmimanager.model.audit.login.LoginStatus;
import com.emmisolutions.emmimanager.model.audit.login.LoginStatusName;
import com.emmisolutions.emmimanager.model.audit.logout.Logout;
import com.emmisolutions.emmimanager.model.audit.logout.LogoutSource;
import com.emmisolutions.emmimanager.model.audit.logout.LogoutSourceName;
import com.emmisolutions.emmimanager.model.user.User;
import com.emmisolutions.emmimanager.persistence.AuthenticationAuditPersistence;
import com.emmisolutions.emmimanager.service.audit.AuthenticationAuditService;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import static com.emmisolutions.emmimanager.service.audit.AuthenticationAuditService.APPLICATION.ADMIN_FACING;

/**
 * Spring bean transacted service for authentication audit
 */
@Service
public class AuthenticationAuditServiceImpl implements AuthenticationAuditService {

    @Resource
    AuthenticationAuditPersistence authenticationAuditPersistence;

    @Async
    @Override
    @Transactional
    public Login login(String loginName, User user, String ipAddress,
                       LoginStatusName status, APPLICATION application) {
        Login login = new Login();
        login.setUser(user);
        login.setLogin(loginName);
        login.setIpAddress(ipAddress);
        if (status != null) {
            login.setStatus(new LoginStatus(status));
        }
        login.setTime(DateTime.now(DateTimeZone.UTC));
        login.setAdminFacingApplication(ADMIN_FACING.equals(application));
        return authenticationAuditPersistence.login(login);
    }

    @Async
    @Override
    @Transactional
    public Logout logout(User user, String ipAddress, LogoutSourceName source, APPLICATION application) {
        Logout logout = new Logout();
        logout.setUser(user);
        logout.setIpAddress(ipAddress);
        if (source != null) {
            logout.setSource(new LogoutSource(source));
        }
        logout.setTime(DateTime.now(DateTimeZone.UTC));
        logout.setAdminFacingApplication(ADMIN_FACING.equals(application));
        return authenticationAuditPersistence.logout(logout);
    }

}
