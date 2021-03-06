package com.emmisolutions.emmimanager.model;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * The Client Tiers
 * <p/>
 * ONE, TWO, THREE, FOUR
 */
@Audited
@Entity
@Table(name = "client_tier", uniqueConstraints = @UniqueConstraint(name = "uk_client_tier_key_path", columnNames = "key_path"))
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ClientTier extends AbstractTypeEntity {

    public ClientTier() {
    }

    /**
     * Primary key constructor
     *
     * @param id the id
     */
    public ClientTier(Long id) {
        setId(id);
    }
}
