package com.emmisolutions.emmimanager.model.audit;

import org.hibernate.envers.RevisionListener;

/**
 * Add the username who has performed the revision change.
 */
public class UsernameRevisionListener implements RevisionListener {

    @Override
    public void newRevision(Object revisionEntity) {
        UsernameRevisionEntity usernameRevisionEntity = (UsernameRevisionEntity) revisionEntity;
        //TODO: pull the username from the logged in user here
        usernameRevisionEntity.setUsername("matty");
    }
}
