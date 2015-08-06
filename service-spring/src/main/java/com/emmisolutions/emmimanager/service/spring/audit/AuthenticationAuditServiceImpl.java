package com.emmisolutions.emmimanager.service.spring.audit;

import com.emmisolutions.emmimanager.model.audit.login.Login;
import com.emmisolutions.emmimanager.model.audit.login.LoginStatus;
import com.emmisolutions.emmimanager.model.audit.login.LoginStatusName;
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
        if (ADMIN_FACING.equals(application)) {
            login.setAdminFacingApplication(true);
        }
        return authenticationAuditPersistence.login(login);
    }

}
