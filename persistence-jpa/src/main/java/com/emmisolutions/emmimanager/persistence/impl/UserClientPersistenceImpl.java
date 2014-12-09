package com.emmisolutions.emmimanager.persistence.impl;

import static org.springframework.data.jpa.domain.Specifications.where;

import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import com.emmisolutions.emmimanager.model.UserClientSearchFilter;
import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.persistence.UserClientPersistence;
import com.emmisolutions.emmimanager.persistence.impl.specification.MatchingCriteriaBean;
import com.emmisolutions.emmimanager.persistence.impl.specification.UserClientSpecifications;
import com.emmisolutions.emmimanager.persistence.repo.UserClientRepository;

/**
 * Repo to deal with user persistence.
 */
@Repository
public class UserClientPersistenceImpl implements UserClientPersistence {

    @Resource
    UserClientRepository userClientRepository;

    @Resource
    UserClientSpecifications userClientSpecifications;

    @Resource
    MatchingCriteriaBean matchCriteria;

    @Override
    public UserClient saveOrUpdate(UserClient user) {
	user.setNormalizedName(matchCriteria.normalizedName(
		user.getFirstName(), user.getLastName(), user.getLogin(),
		user.getEmail()));
	return userClientRepository.save(user);
    }

    @Override
    public Page<UserClient> list(Pageable pageable,
	    UserClientSearchFilter filter) {
	if (pageable == null) {
	    pageable = new PageRequest(0, 10, Sort.Direction.ASC, "id");
	}
	return userClientRepository.findAll(
		where(userClientSpecifications.hasNames(filter)).and(
			userClientSpecifications.isClient(filter)), pageable);
    }

}
