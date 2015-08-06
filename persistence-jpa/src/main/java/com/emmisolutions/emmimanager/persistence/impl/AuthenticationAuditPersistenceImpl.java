package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.audit.login.Login;
import com.emmisolutions.emmimanager.model.audit.logout.Logout;
import com.emmisolutions.emmimanager.persistence.AuthenticationAuditPersistence;
import com.emmisolutions.emmimanager.persistence.repo.*;
import org.apache.commons.lang3.StringUtils;
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

    @Resource
    LoginStatusRepository loginStatusRepository;

    @Resource
    LogoutSourceRepository logoutSourceRepository;

    @Resource
    UserClientRepository userClientRepository;

    @Resource
    UserAdminRepository userAdminRepository;

    @Override
    public Login login(Login attempt) {
        if (attempt == null) {
            return null;
        }
        if (attempt.getUser() != null && attempt.getUser().getId() != null) {
            // find a user by id
            attempt.setUser(userRepository.findOne(attempt.getUser().getId()));
        }
        if (StringUtils.isNotBlank(attempt.getLogin()) && attempt.getUser() == null) {
            // attempt to find a user client via the login
            attempt.setUser(userClientRepository.findByLoginIgnoreCase(attempt.getLogin()));
        }
        if (StringUtils.isNotBlank(attempt.getLogin()) && attempt.getUser() == null) {
            // attempt to find the user admin via the login
            attempt.setUser(userAdminRepository.fetchWithFullPermissions(attempt.getLogin()));
        }
        if (attempt.getStatus() != null && attempt.getStatus().getName() != null) {
            attempt.setStatus(loginStatusRepository.findByName(attempt.getStatus().getName()));
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
        if (attempt.getSource() != null && attempt.getSource().getName() != null) {
            attempt.setSource(logoutSourceRepository.findByName(attempt.getSource().getName()));
        }
        return logoutRepository.save(attempt);
    }

}
