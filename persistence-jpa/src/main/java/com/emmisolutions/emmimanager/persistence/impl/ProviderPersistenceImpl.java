package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.Provider;
import com.emmisolutions.emmimanager.model.ProviderSearchFilter;
import com.emmisolutions.emmimanager.model.ProviderSpecialty;
import com.emmisolutions.emmimanager.persistence.ProviderPersistence;
import com.emmisolutions.emmimanager.persistence.impl.specification.ProviderSpecifications;
import com.emmisolutions.emmimanager.persistence.repo.ProviderRepository;
import com.emmisolutions.emmimanager.persistence.repo.ProviderSpecialtyRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

import static org.springframework.data.jpa.domain.Specifications.where;

/**
 * Provider Persistence Implementation
 */
@Repository
public class ProviderPersistenceImpl implements ProviderPersistence {

    @Resource
    ProviderRepository providerRepository;

    @Resource
    ProviderSpecialtyRepository providerSpecialtyRepository;

    @Resource
    ProviderSpecifications providerSpecifications;


    @Override
    public ProviderSpecialty saveSpecialty(ProviderSpecialty provider) {
        return providerSpecialtyRepository.save(provider);
    }

    @Override
    public Provider save(Provider provider) {
        provider.setFullName(null);
        provider.setNormalizedName(normalizeName(provider.getFullName()));
        return providerRepository.save(provider);
    }

    @Override
    public Provider reload(Provider provider) {
        if (provider == null || provider.getId() == null) {
            return null;
        }
        return providerRepository.findOne(provider.getId());
    }

    @Override
    public Page<Provider> list(Pageable page, ProviderSearchFilter filter) {
        if (page == null) {
            // default pagination request if none
            page = new PageRequest(0, 50, Sort.Direction.ASC, "id");
        }
        return providerRepository.findAll(where(
            providerSpecifications.notUsedBy(filter))
            .and(providerSpecifications.hasNames(filter))
            .and(providerSpecifications.isInStatus(filter)), page);

    }

    @Override
    public Page<ProviderSpecialty> findAllProviderSpecialties(Pageable page) {
        if (page == null) {
            // default pagination request if none
            page = new PageRequest(0, 50, Sort.Direction.ASC, "name");
        }
        return providerSpecialtyRepository.findAll(page);
    }
    /**
     * remove the special characters replacing it with blank (" ") and change all to lower case
     *
     * @param name name
     * @return the string
     */
    private String normalizeName(String name) {
        String normalizedName = StringUtils.trimToEmpty(StringUtils.lowerCase(name));
        if (StringUtils.isNotBlank(normalizedName)) {
            // do regex
            normalizedName = normalizedName.replaceAll("[^a-z0-9]*", "");
        }
        return normalizedName;
    }

}
