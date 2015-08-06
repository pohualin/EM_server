package com.emmisolutions.emmimanager.service.audit;

import com.emmisolutions.emmimanager.model.audit.login.Login;
import com.emmisolutions.emmimanager.model.audit.login.LoginStatusName;
import com.emmisolutions.emmimanager.model.user.User;

/**
 * Service to audit authentication based events
 */
public interface AuthenticationAuditService {

    Login login(String loginName, User user, String ipAddress, LoginStatusName status, APPLICATION application);

    enum APPLICATION {
        ADMIN_FACING, CLIENT_FACING
    }
}
