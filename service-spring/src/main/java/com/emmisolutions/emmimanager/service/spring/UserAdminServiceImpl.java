package com.emmisolutions.emmimanager.service.spring;

import com.emmisolutions.emmimanager.model.UserAdminSaveRequest;
import com.emmisolutions.emmimanager.model.UserAdminSearchFilter;
import com.emmisolutions.emmimanager.model.user.admin.UserAdmin;
import com.emmisolutions.emmimanager.model.user.admin.UserAdminRole;
import com.emmisolutions.emmimanager.model.user.admin.UserAdminUserAdminRole;
import com.emmisolutions.emmimanager.persistence.UserAdminPersistence;
import com.emmisolutions.emmimanager.service.UserAdminService;
import com.emmisolutions.emmimanager.service.security.UserDetailsService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @Override
    @Transactional
    public UserAdmin save(UserAdminSaveRequest req) {
        Set<UserAdminUserAdminRole> roles = new HashSet<>();
        for (UserAdminRole userAdminRole : req.getRoles()) {
            UserAdminUserAdminRole userAdminUserAdminRole = new UserAdminUserAdminRole();
            userAdminUserAdminRole.setUserAdmin(req.getUserAdmin());
            userAdminUserAdminRole.setUserAdminRole(userAdminRole);
            roles.add(userAdminUserAdminRole);
        }

        UserAdmin user = req.getUserAdmin();

        boolean modificationOfLoggedInUser = false;

        UserAdmin userFromDb = userAdminPersistence.reload(user);
        if (userFromDb != null) {
            modificationOfLoggedInUser = userFromDb.equals(userDetailsService.getLoggedInUser());
            // don't allow password change on update
            user.setPassword(userFromDb.getPassword());
            user.setSalt(userFromDb.getSalt());
            user.setCredentialsNonExpired(true);
        } else {
            // new user, don't allow passwords to be set here
            user.setPassword(null);
            user.setSalt(null);
            user.setCredentialsNonExpired(true);
        }

        // save the updates
        user = userAdminPersistence.saveOrUpdate(user);

        // don't allow a user to escalate or change their own role
        if (!modificationOfLoggedInUser && roles.size() > 0) {
            userAdminPersistence.removeAllAdminRoleByUserAdmin(user);
            userAdminPersistence.saveAll(roles);
            user.setRoles(roles);
        }
        return user;
    }

    @Override
    @Transactional
    public UserAdmin reload(UserAdmin user) {
        if (user == null || user.getId() == null) {
            return null;
        }
        return userAdminPersistence.reload(user);
    }

    @Override
    @Transactional
    public UserAdmin fetchUserWillFullPermissions(UserAdmin user) {
        if (user == null || user.getId() == null) {
            return null;
        }
        user = userAdminPersistence.reload(user);
        return userAdminPersistence.fetchUserWillFullPermissions(user.getLogin());
    }

    /**
     * @param page             Page number of users needed
     * @param userSearchFilter search filter for teams
     * @return a particular page of users
     */
    @Override
    @Transactional(readOnly = true)
    public Page<UserAdmin> list(Pageable page, UserAdminSearchFilter userSearchFilter) {
        return userAdminPersistence.list(page, userSearchFilter);
    }

    @Override
    public Page<UserAdminRole> listRolesWithoutSystem(Pageable pageable) {
        return userAdminPersistence.listRolesWithoutSystem(pageable);
    }

    @Override
    @Transactional
    public List<UserAdmin> findConflictingUsers(UserAdmin userAdmin) {
        return new ArrayList<>(userAdminPersistence.findConflictingUsers(userAdmin));
    }

}
