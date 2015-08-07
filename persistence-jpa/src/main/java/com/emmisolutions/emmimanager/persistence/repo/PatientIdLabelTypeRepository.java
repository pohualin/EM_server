package com.emmisolutions.emmimanager.persistence.repo;

import com.emmisolutions.emmimanager.model.PatientIdLabelType;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * PatientIdLabelType repo.
 */
public interface PatientIdLabelTypeRepository extends JpaRepository<PatientIdLabelType, Long>, JpaSpecificationExecutor<PatientIdLabelType> {

    @Override
    @Cacheable(value = "allPatientIdLabelTypes")
    List<PatientIdLabelType> findAll();
}
