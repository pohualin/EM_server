package com.emmisolutions.emmimanager.persistence.impl;


import static org.springframework.data.jpa.domain.Specifications.where;

import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import com.emmisolutions.emmimanager.model.Location;
import com.emmisolutions.emmimanager.model.LocationSearchFilter;
import com.emmisolutions.emmimanager.model.Team;
import com.emmisolutions.emmimanager.persistence.LocationPersistence;
import com.emmisolutions.emmimanager.persistence.impl.specification.LocationSpecifications;
import com.emmisolutions.emmimanager.persistence.impl.specification.MatchingCriteriaBean;
import com.emmisolutions.emmimanager.persistence.repo.LocationRepository;

/**
 * Location Persistence API implementation
 */
@Repository
public class LocationPersistenceImpl implements LocationPersistence {

    @Resource
    LocationSpecifications locationSpecifications;

    @Resource
    LocationRepository locationRepository;

    @Resource
    MatchingCriteriaBean matchCriteria;
    
    @Override
    public Page<Location> list(Pageable page, LocationSearchFilter filter) {
        if (page == null) {
            // default pagination request if none
            page = new PageRequest(0, 50, Sort.Direction.ASC, "id");
        }

        return locationRepository.findAll(where(
            locationSpecifications.notUsedBy(filter))
            .and(locationSpecifications.belongsTo(filter))
            .and(locationSpecifications.hasNames(filter))
            .and(locationSpecifications.isInStatus(filter)), page);
    }
    
    @Override
    public Location save(Location location) {
    	location.setNormalizedName(normalizeName(location));
        return locationRepository.save(location);
    }

    @Override
    public Location reload(Location location) {
        if (location == null || location.getId() == null) {
            return null;
        }
        return locationRepository.findOne(location.getId());
    }

    private String normalizeName(Location location){
    	return matchCriteria.normalizeNameAndBlank(location.getName()==null?"":location.getName());
    }
}
