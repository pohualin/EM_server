package com.emmisolutions.emmimanager.model;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * The Client Regions
 * <p/>
 * OTHER, SOUTHEAST, NORTHEAST, MIDWEST, WEST
 */
@Audited
@Entity
@Table(name = "client_region", uniqueConstraints = @UniqueConstraint(name = "uk_client_region_key_path", columnNames = "key_path"))
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ClientRegion extends AbstractTypeEntity {

    public ClientRegion() {
    }

    /**
     * Primary key constructor
     *
     * @param id the id
     */
    public ClientRegion(Long id) {
        setId(id);
    }

}

