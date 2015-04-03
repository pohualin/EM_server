package com.emmisolutions.emmimanager.service.spring;

import com.emmisolutions.emmimanager.model.Patient;
import com.emmisolutions.emmimanager.model.Provider;
import com.emmisolutions.emmimanager.persistence.PatientPersistence;
import com.emmisolutions.emmimanager.service.PatientService;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

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
        if (patient == null || patient.getId() == null) {
            return null;
        }
        return patientPersistence.reload(patient);
    }
}
