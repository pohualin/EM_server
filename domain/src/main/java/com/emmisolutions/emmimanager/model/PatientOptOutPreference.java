package com.emmisolutions.emmimanager.model;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * The Patient Opt Out Preference Types
 * <p/>
 * WHOLE_PATIENT, PHONE, EMAIL
 */
@Audited
@Entity
@Table(name = "patient_opt_out_preference",
        uniqueConstraints = @UniqueConstraint(name = "uk_client_type_key_path", columnNames = "key_path"))
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class PatientOptOutPreference extends AbstractTypeEntity {

}
