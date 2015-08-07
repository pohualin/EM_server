package com.emmisolutions.emmimanager.service.audit;

import com.emmisolutions.emmimanager.model.audit.login.Login;
import com.emmisolutions.emmimanager.model.audit.login.LoginStatusName;
import com.emmisolutions.emmimanager.model.audit.logout.Logout;
import com.emmisolutions.emmimanager.model.audit.logout.LogoutSourceName;
import com.emmisolutions.emmimanager.model.user.User;

/**
 * Service to audit authentication based events
 */
public interface AuthenticationAuditService {

    /**
     * Records a login
     *
     * @param loginName   the login name
     * @param user        the domain user
     * @param ipAddress   the ip address of the person requesting login
     * @param status      the login status
     * @param application which application is the login for
     * @return the persistent Login object
     */
    Login login(String loginName, User user, String ipAddress, LoginStatusName status, APPLICATION application);

    /**
     * Records a logout
     *
     * @param user        the domain user
     * @param ipAddress   the ip address of the person requesting logout
     * @param source      the source of the logout
     * @param application which application is this logout for
     * @return the persistent Logout object
     */
    Logout logout(User user, String ipAddress, LogoutSourceName source, APPLICATION application);

    enum APPLICATION {
        ADMIN_FACING, CLIENT_FACING
    }
}
