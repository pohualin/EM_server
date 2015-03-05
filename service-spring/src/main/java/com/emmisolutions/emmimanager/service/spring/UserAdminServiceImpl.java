package com.emmisolutions.emmimanager.service.spring;

import com.emmisolutions.emmimanager.model.UserAdminSaveRequest;
import com.emmisolutions.emmimanager.model.UserAdminSearchFilter;
import com.emmisolutions.emmimanager.model.user.admin.UserAdmin;
import com.emmisolutions.emmimanager.model.user.admin.UserAdminRole;
import com.emmisolutions.emmimanager.model.user.admin.UserAdminUserAdminRole;
import com.emmisolutions.emmimanager.persistence.UserAdminPersistence;
import com.emmisolutions.emmimanager.service.UserAdminService;
import com.emmisolutions.emmimanager.service.security.UserDetailsService;
import com.emmisolutions.emmimanager.service.spring.security.LegacyPasswordEncoder;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * An example of a Service class. It can only contact the persistence layer
 * and is responsible for Transaction demarcation. This layer will also have
 * security annotations at the method level as well.
 */
@Service
public class UserAdminServiceImpl implements UserAdminService {

    @Resource
    UserAdminPersistence userAdminPersistence;

    @Resource(name = "adminUserDetailsService")
    UserDetailsService userDetailsService;

    @Resource
    PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserAdmin save(UserAdminSaveRequest req) {
        if (req == null || req.getUserAdmin() == null ||
                userAdminPersistence.isSystemUser(req.getUserAdmin())) {
            throw new InvalidDataAccessApiUsageException("Invalid save request");
        }

        UserAdmin user = req.getUserAdmin();
        boolean modificationOfLoggedInUser = false;
        UserAdmin userFromDb = userAdminPersistence.reload(user);
        if (userFromDb != null) {
            // this is an update
            modificationOfLoggedInUser = userFromDb.equals(userDetailsService.getLoggedInUser());
            // override values that cannot be changed during save
            user.setPassword(userFromDb.getPassword());
            user.setSalt(userFromDb.getSalt());
            user.setCredentialsNonExpired(true);
            user.setAccountNonExpired(userFromDb.isAccountNonExpired());
            user.setAccountNonLocked(userFromDb.isAccountNonLocked());
            if (modificationOfLoggedInUser) {
                // don't allow change of web-api or active if the user is changing themselves
                user.setWebApiUser(userFromDb.isWebApiUser());
                user.setActive(userFromDb.isActive());
            }
            if (!user.isWebApiUser()) {
                // remove passwords for non-web-api users
                user.setPassword(null);
                user.setSalt(null);
            }
        } else {
            // new user, don't allow passwords to be set here
            user.setPassword(null);
            user.setSalt(null);
            user.setCredentialsNonExpired(true);
        }

        // save the updates
        user = userAdminPersistence.saveOrUpdate(user);

        // update the user roles, when not changing the logged in user
        if (!modificationOfLoggedInUser && !CollectionUtils.isEmpty(req.getRoles())) {
            List<UserAdminUserAdminRole> roles = new ArrayList<>();
            for (UserAdminRole userAdminRole : req.getRoles()) {
                UserAdminUserAdminRole userAdminUserAdminRole = new UserAdminUserAdminRole();
                userAdminUserAdminRole.setUserAdmin(req.getUserAdmin());
                userAdminUserAdminRole.setUserAdminRole(userAdminRole);
                roles.add(userAdminUserAdminRole);
            }
            userAdminPersistence.removeAllAdminRoleByUserAdmin(user);
            user.setRoles(userAdminPersistence.saveAll(roles));
        }

        return user;
    }

    @Override
    @Transactional(readOnly = true)
    public UserAdmin reload(UserAdmin user) {
        return userAdminPersistence.reload(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserAdmin fetchUserWillFullPermissions(UserAdmin user) {
        UserAdmin fromDb = userAdminPersistence.reload(user);
        if (fromDb == null) {
            return null;
        }
        return userAdminPersistence.fetchUserWillFullPermissions(fromDb.getLogin());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserAdmin> list(Pageable page, UserAdminSearchFilter userSearchFilter) {
        return userAdminPersistence.list(page, userSearchFilter);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserAdminRole> listRolesWithoutSystem(Pageable pageable) {
        return userAdminPersistence.listRolesWithoutSystem(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserAdmin> findConflictingUsers(UserAdmin userAdmin) {
        return new ArrayList<>(userAdminPersistence.findConflictingUsers(userAdmin));
    }

    @Override
    @Transactional
    public UserAdmin updatePassword(UserAdmin userAdmin) {
        UserAdmin inDb = reload(userAdmin);
        if (inDb != null && inDb.isWebApiUser()) {
            inDb.setPassword(userAdmin.getPassword());
            return userAdminPersistence.saveOrUpdate(encodePassword(inDb));
        }
        return null;
    }

    private UserAdmin encodePassword(UserAdmin userAdmin) {
        String encodedPasswordPlusSalt = passwordEncoder.encode(userAdmin.getPassword());
        userAdmin.setPassword(encodedPasswordPlusSalt.substring(0, LegacyPasswordEncoder.PASSWORD_SIZE));
        userAdmin.setSalt(encodedPasswordPlusSalt.substring(LegacyPasswordEncoder.PASSWORD_SIZE));
        return userAdmin;
    }

}
