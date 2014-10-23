package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.*;
import com.emmisolutions.emmimanager.persistence.ProviderPersistence;
import com.emmisolutions.emmimanager.persistence.repo.ProviderRepository;
import com.emmisolutions.emmimanager.persistence.repo.ReferenceTagRepository;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.QueryHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import static com.emmisolutions.emmimanager.persistence.impl.specification.ProviderSpecifications.hasNames;
import static com.emmisolutions.emmimanager.persistence.impl.specification.ProviderSpecifications.isInStatus;
import static org.springframework.data.jpa.domain.Specifications.where;

/**
 * Provider Persistence Implementation
 *
 */
@Repository
public class ProviderPersistenceImpl implements ProviderPersistence {

	@Resource
	ProviderRepository providerRepository;

    @Resource
    ReferenceTagRepository referenceTagRepository;
    
    @PersistenceContext
    EntityManager entityManager;
	
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
	public Page<Provider> findAllProvidersByTeam(Pageable page, Team team) {
        if (page == null) {
            // default pagination request if none
            page = new PageRequest(0, 50, Sort.Direction.ASC, "name");
        }
		return providerRepository.findByTeams(page, team);
	}
	
	@Override
    @SuppressWarnings("unchecked")
	public Page<Provider> list(Pageable page, ProviderSearchFilter filter) {
		if (page == null) {
            // default pagination request if none
            page = new PageRequest(0, 50, Sort.Direction.ASC, "id");
        }

        Page<Provider> ret = providerRepository.findAll(where(hasNames(filter)).and(isInStatus(filter)), page);
        
        if (ret.hasContent()) {
      		 CriteriaBuilder cb = entityManager.getCriteriaBuilder();
      		 EntityGraph<Provider> graph = entityManager.createEntityGraph(Provider.class);
      		 graph.addAttributeNodes(Provider_.teams);
      		 CriteriaQuery<Provider> cq = cb.createQuery(Provider.class);
      		 Root<Provider> root = cq.from(Provider.class);
      		 cq.select(root).where(root.in(ret.getContent()));
      		 entityManager.createQuery(cq).setHint(QueryHints.LOADGRAPH, graph).getResultList();
        }
        
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
    @Override
    public Page<ReferenceTag> findAllByGroupTypeName(Pageable page) {
        if (page == null) {
            // default pagination request if none
            page = new PageRequest(0, 50, Sort.Direction.ASC, "name");
        }
        return referenceTagRepository.findAllByGroupTypeName(SPECIALTY, page);
    }

}
