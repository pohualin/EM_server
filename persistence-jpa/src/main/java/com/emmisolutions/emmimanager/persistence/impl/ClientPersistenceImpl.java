package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.*;
import com.emmisolutions.emmimanager.persistence.ClientPersistence;
import com.emmisolutions.emmimanager.persistence.repo.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.emmisolutions.emmimanager.persistence.impl.specification.ClientSpecifications.hasNames;
import static com.emmisolutions.emmimanager.persistence.impl.specification.ClientSpecifications.isInStatus;
import static org.springframework.data.jpa.domain.Specifications.where;

/**
 * Client persistence implementation.
 */
@Repository
public class ClientPersistenceImpl implements ClientPersistence {

    @Resource
    ClientRepository clientRepository;

    @Resource
    ClientTypeRepository clientTypeRepository;

    @Resource
    ClientRegionRepository clientRegionRepository;

    @Resource
    ClientTierRepository clientTierRepository;

    @Resource
    UserRepository userRepository;

    @Override
    public Page<Client> list(Pageable page, ClientSearchFilter searchFilter) {
        if (page == null) {
            // default pagination request if none
            page = new PageRequest(0, 50, Sort.Direction.ASC, "id");
        }
        return clientRepository.findAll(
                where(hasNames(searchFilter)).and(isInStatus(searchFilter)),
                caseInsensitiveSort(page));
    }

    public Client save(Client client) {
        client.setNormalizedName(normalizeName(client));
        return clientRepository.save(client);
    }

    @Override
    public Client reload(Long id) {
        return clientRepository.findOne(id);
    }

    @Override
    public Client findByNormalizedName(String normalizedName) {
        String toSearch = normalizeName(normalizedName);
        Client ret = null;
        if (StringUtils.isNotBlank(toSearch)) {
            ret = clientRepository.findByNormalizedName(toSearch);
        }
        return ret;
    }

    @Override
    public Collection<ClientType> getAllClientTypes() {
        return clientTypeRepository.findAll();
    }

    @Override
    public Collection<ClientRegion> getAllRegionTypes() {
        return clientRegionRepository.findAll();
    }

    @Override
    public Collection<ClientTier> getAllClientTiers() {
        return clientTierRepository.findAll();
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

    /**
     * Takes all sort parameters and makes them case insensitive sorts
     *
     * @param page copy of the existing pageable request with insensitive sorts
     * @return Pageable
     */
    private Pageable caseInsensitiveSort(Pageable page) {
        Sort sort = null;
        if (page.getSort() != null) {
            List<Sort.Order> insensitiveOrders = new ArrayList<>();
            for (Sort.Order pageSort : page.getSort()) {
                insensitiveOrders.add(new Sort.Order(pageSort.getDirection(), pageSort.getProperty()).ignoreCase());
            }
            sort = new Sort(insensitiveOrders);
        }
        return new PageRequest(page.getPageNumber(), page.getPageSize(), sort);
    }

    private String normalizeName(Client client) {
        return normalizeName(client.getName() == null ? "" : client.getName());
    }

}
