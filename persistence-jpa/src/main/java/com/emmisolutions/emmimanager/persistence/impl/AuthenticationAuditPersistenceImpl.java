package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.audit.login.Login;
import com.emmisolutions.emmimanager.model.audit.logout.Logout;
import com.emmisolutions.emmimanager.persistence.AuthenticationAuditPersistence;
import com.emmisolutions.emmimanager.persistence.repo.LoginRepository;
import com.emmisolutions.emmimanager.persistence.repo.LogoutRepository;
import com.emmisolutions.emmimanager.persistence.repo.UserRepository;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * Spring data implementation
 */
@Repository
public class AuthenticationAuditPersistenceImpl implements AuthenticationAuditPersistence {

    @Resource
    LoginRepository loginRepository;

    @Resource
    LogoutRepository logoutRepository;

    @Resource
    UserRepository userRepository;

    @Override
    public Login login(Login attempt) {
        if (attempt == null) {
            return null;
        }
        if (attempt.getUser() != null && attempt.getUser().getId() != null) {
            attempt.setUser(userRepository.findOne(attempt.getUser().getId()));
        }
        return loginRepository.save(attempt);
    }

    @Override
    public Logout logout(Logout attempt) {
        if (attempt == null) {
            return null;
        }
        if (attempt.getUser() != null && attempt.getUser().getId() != null) {
            attempt.setUser(userRepository.findOne(attempt.getUser().getId()));
        }
        return logoutRepository.save(attempt);
    }
}
