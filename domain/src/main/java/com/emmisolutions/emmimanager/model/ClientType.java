package com.emmisolutions.emmimanager.model;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * The Client Types
 * <p/>
 * PROVIDER, PHYSICIAN_PRACTICE, PAYER, PHARMA, OTHER
 */
@Audited
@Entity
@Table(name = "client_type", uniqueConstraints = @UniqueConstraint(name = "uk_client_type_key_path", columnNames = "key_path"))
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ClientType extends AbstractTypeEntity {

    public ClientType() {
    }

    /**
     * Primary key constructor
     *
     * @param id the id
     */
    public ClientType(Long id) {
        setId(id);
    }


}
