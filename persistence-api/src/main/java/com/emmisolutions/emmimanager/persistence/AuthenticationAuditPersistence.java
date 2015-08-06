package com.emmisolutions.emmimanager.persistence;

import com.emmisolutions.emmimanager.model.audit.login.Login;
import com.emmisolutions.emmimanager.model.audit.logout.Logout;

/**
 * Persistence for authentication events.
 */
public interface AuthenticationAuditPersistence {

    Login login(Login attempt);

    Logout logout(Logout attempt);

}
