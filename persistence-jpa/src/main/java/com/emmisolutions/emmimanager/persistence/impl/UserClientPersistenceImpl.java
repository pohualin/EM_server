package com.emmisolutions.emmimanager.persistence.impl;

import static org.springframework.data.jpa.domain.Specifications.where;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import com.emmisolutions.emmimanager.model.UserClientSearchFilter;
import com.emmisolutions.emmimanager.model.user.client.UserClient;
import com.emmisolutions.emmimanager.persistence.UserClientPersistence;
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

    @Override
    public UserClient saveOrUpdate(UserClient user) {
	user.setNormalizedName(normalizeName(user));
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
			userClientSpecifications.isClient(filter)),
		caseInsensitiveSort(pageable));
    }

    /**
     * Takes all sort parameters and makes them case insensitive sorts
     *
     * @param page
     *            copy of the existing pageable request with insensitive sorts
     * @return Pageable
     */
    private Pageable caseInsensitiveSort(Pageable page) {
	Sort sort = null;
	if (page.getSort() != null) {
	    List<Sort.Order> insensitiveOrders = new ArrayList<>();
	    for (Sort.Order pageSort : page.getSort()) {
		insensitiveOrders.add(new Sort.Order(pageSort.getDirection(),
			pageSort.getProperty()).ignoreCase());
	    }
	    sort = new Sort(insensitiveOrders);
	}
	return new PageRequest(page.getPageNumber(), page.getPageSize(), sort);
    }

    /**
     * remove the special characters replacing it with blank (" ") and change
     * all to lower case
     *
     * @param name
     * @return
     */
    private String normalizeName(UserClient user) {
	StringBuilder sb = new StringBuilder();
	if(StringUtils.isNotBlank(user.getFirstName())){
	    sb.append(user.getFirstName());
	}
	if(StringUtils.isNotBlank(user.getLastName())){
	    sb.append(user.getLastName());
	}
	if(StringUtils.isNotBlank(user.getLogin())){
	    sb.append(user.getLogin());
	}
	if(StringUtils.isNotBlank(user.getEmail())){
	    sb.append(user.getEmail());
	}
	String normalizedName = StringUtils.trimToEmpty(StringUtils
		.lowerCase(sb.toString()));
	if (StringUtils.isNotBlank(normalizedName)) {
	    normalizedName = normalizedName.replaceAll("[^a-z0-9]*", "");
	}
	return normalizedName;
    }

}
