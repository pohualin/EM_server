package com.emmisolutions.emmimanager.service.spring;

import com.emmisolutions.emmimanager.model.Patient;
import com.emmisolutions.emmimanager.model.PatientOptOutPreference;
import com.emmisolutions.emmimanager.model.PatientSearchFilter;
import com.emmisolutions.emmimanager.persistence.PatientPersistence;
import com.emmisolutions.emmimanager.service.PatientService;
import org.joda.time.LocalDate;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collection;

/**
 * Implementation for Patient Service
 */
@Service
public class PatientServiceImpl implements PatientService {

    @Resource
    PatientPersistence patientPersistence;

    @Override
    @Transactional
    public Patient create(Patient patient) {
        if (patient == null) {
            throw new InvalidDataAccessApiUsageException("patient cannot be null");
        }

        if (patient.getDateOfBirth() != null && (patient.getDateOfBirth().isBefore(LocalDate.now().minusYears(125)))){
            throw new InvalidDataAccessApiUsageException("patient cannot be more than 125 years old");
        }
        patient.setId(null);
        patient.setVersion(null);
        return patientPersistence.save(patient);
    }

    @Override
    @Transactional
    public Patient update(Patient patient) {
        if (patient == null || patient.getId() == null || patient.getVersion() == null) {
            throw new InvalidDataAccessApiUsageException("patient Id and Version cannot be null.");
        }
        return patientPersistence.save(patient);
    }

    @Override
    @Transactional(readOnly = true)
    public Patient reload(Patient patient) {
        return patientPersistence.reload(patient);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Patient> list(Pageable page,  PatientSearchFilter patientSearchFilter) {
        return patientPersistence.list(page, patientSearchFilter);
    }

    @Override
    public Collection<PatientOptOutPreference> allPossibleOptOutPreferences() {
        return patientPersistence.allPossibleOptOutPreferences();
    }
}
