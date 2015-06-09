package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.Patient;
import com.emmisolutions.emmimanager.model.PatientSearchFilter;
import com.emmisolutions.emmimanager.persistence.PatientPersistence;
import com.emmisolutions.emmimanager.persistence.impl.specification.PatientSpecifications;
import com.emmisolutions.emmimanager.persistence.repo.PatientRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

import static org.springframework.data.jpa.domain.Specifications.where;

/**
 * Patient Persistence Implementation
 */
@Repository
public class PatientPersistenceImpl implements PatientPersistence {

    @Resource
    PatientRepository patientRepository;

    @Resource
    PatientSpecifications patientSpecifications;

    @Override
    public Patient save(Patient patient) {
        patient.setNormalizedName(normalizeName(patient));
        return patientRepository.save(patient);
    }

    private String normalizeName(String name) {
        String normalizedName = StringUtils.trimToEmpty(StringUtils.lowerCase(name));
        if (StringUtils.isNotBlank(normalizedName)) {
            // do regex
            normalizedName = normalizedName.replaceAll("[^a-z0-9]*", "");
        }
        return normalizedName;
    }

    private String normalizeName(Patient patient) {
        return normalizeName((patient.getFirstName() != null && patient.getLastName() != null) ? patient.getFirstName() + patient.getLastName()
                : patient.getFirstName() == null ? patient.getLastName() == null ? "" : patient.getLastName() : patient.getFirstName());
    }

    @Override
    public Patient reload(Patient patient) {
        if (patient == null || patient.getId() == null) {
            return null;
        }
        return patientRepository.findOne(patient.getId());
    }

    @Override
    public Page<Patient> list(Pageable page, PatientSearchFilter filter) {
        if (page == null) {
            // default pagination request if none
            page = new PageRequest(0, 50, Sort.Direction.ASC, "id");
        }
        return patientRepository.findAll(where(patientSpecifications.hasNames(filter))
                        .and(patientSpecifications.belongsTo(filter))
                        .and(patientSpecifications.withPhoneNumber(filter))
                        .and(patientSpecifications.withEmail(filter))
                        .and(patientSpecifications.withAccessCodes(filter)),
                page);

    }
}
