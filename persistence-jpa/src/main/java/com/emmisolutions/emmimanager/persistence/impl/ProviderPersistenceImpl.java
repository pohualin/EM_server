package com.emmisolutions.emmimanager.persistence.impl;

import static com.emmisolutions.emmimanager.persistence.impl.specification.ProviderSpecifications.hasNames;
import static com.emmisolutions.emmimanager.persistence.impl.specification.ProviderSpecifications.isInStatus;
import static org.springframework.data.jpa.domain.Specifications.where;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import com.emmisolutions.emmimanager.model.Provider;
import com.emmisolutions.emmimanager.model.ProviderSearchFilter;
import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.persistence.ProviderPersistence;
import com.emmisolutions.emmimanager.persistence.repo.ProviderRepository;

/**
 * Provider Persistence Implementation
 *
 */
@Repository
public class ProviderPersistenceImpl implements ProviderPersistence {

	@Resource
	ProviderRepository providerRepository;

	@Override
	public Provider save(Provider provider) {
		provider.setNormalizedName(normalizeName(provider));
		return providerRepository.save(provider);
	}

	@Override
	public Provider reload(Long id) {
		return providerRepository.findOne(id);
	}

	@Override
	public Page<Provider> findAllProvidersByTeam(Pageable pageble, Team team) {
		return providerRepository.findByTeams(pageble, team);
	}
	
	@Override
	public Page<Provider> list(Pageable page, ProviderSearchFilter filter) {
		if (page == null) {
            // default pagination request if none
            page = new PageRequest(0, 50, Sort.Direction.ASC, "id");
        }

        Page<Provider> ret = providerRepository.findAll(where(hasNames(filter)).and(isInStatus(filter)), page);

        return ret;
	}

    /**
     * remove the special characters replacing it with blank (" ") and change all to lower case
     *
     * @param name
     * @return
     */
    private String normalizeName(String name) {
        String normalizedName = StringUtils.trimToEmpty(StringUtils.lowerCase(name));
        if (StringUtils.isNotBlank(normalizedName)) {
            // do regex
            normalizedName = normalizedName.replaceAll("[^a-z0-9]*", "");
        }
        return normalizedName;
    }
    
    private String normalizeName(Provider provider) {
      return normalizeName((provider.getFirstName() != null && provider.getLastName() != null) ? provider.getFirstName() + provider.getLastName() 
    		  : provider.getFirstName() == null ? provider.getLastName() == null ? "" : provider.getLastName() : provider.getFirstName());
    }
}
