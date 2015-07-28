package com.emmisolutions.emmimanager.model.audit;

import com.emmisolutions.emmimanager.model.user.AnonymousUser;
import com.emmisolutions.emmimanager.model.user.User;
import org.hibernate.envers.RevisionListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Add the username who has performed the revision change.
 */
public class UsernameRevisionListener implements RevisionListener {

    @Override
    public void newRevision(Object revisionEntity) {
        UsernameRevisionEntity usernameRevisionEntity = (UsernameRevisionEntity) revisionEntity;
        usernameRevisionEntity.setUser(getCurrentLogin());
    }

    private User getCurrentLogin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() ||
                authentication.getPrincipal() instanceof AnonymousUser) {
            return null;
        }
        return (User) authentication.getPrincipal();
    }
}
