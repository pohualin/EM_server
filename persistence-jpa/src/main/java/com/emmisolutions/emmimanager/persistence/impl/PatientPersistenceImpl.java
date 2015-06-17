package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.Patient;
import com.emmisolutions.emmimanager.model.PatientOptOutPreference;
import com.emmisolutions.emmimanager.model.PatientSearchFilter;
import com.emmisolutions.emmimanager.model.schedule.ScheduledProgram;
import com.emmisolutions.emmimanager.model.schedule.ScheduledProgram_;
import com.emmisolutions.emmimanager.persistence.PatientPersistence;
import com.emmisolutions.emmimanager.persistence.impl.specification.PatientSpecifications;
import com.emmisolutions.emmimanager.persistence.repo.PatientOptOutPreferenceRepository;
import com.emmisolutions.emmimanager.persistence.repo.PatientRepository;
import com.emmisolutions.emmimanager.persistence.repo.ScheduledProgramRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.util.*;

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

    @Resource
    ScheduledProgramRepository scheduledProgramRepository;

    @Resource
    PatientOptOutPreferenceRepository patientOptOutPreferenceRepository;

    @PersistenceContext
    EntityManager entityManager;

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
        final Page<Patient> ret = patientRepository.findAll(where(patientSpecifications.hasNames(filter))
                        .and(patientSpecifications.belongsTo(filter))
                        .and(patientSpecifications.withPhoneNumber(filter))
                        .and(patientSpecifications.withEmail(filter))
                        .and(patientSpecifications.withAccessCodes(filter))
                        .and(patientSpecifications.scheduledForTeams(filter)),
                page);

        // if indicated by the filter, find the latest scheduled program for each patient
        Map<Patient, ScheduledProgram> latestScheduledProgramMap = new HashMap<>();
        if (filter != null && filter.needToLoadLastScheduledProgram() && ret.hasContent()) {
            for (ScheduledProgram scheduledProgram :
                    scheduledProgramRepository.findAll(new Specification<ScheduledProgram>() {
                        @Override
                        public Predicate toPredicate(Root<ScheduledProgram> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                            // find maximum (latest) scheduled program id, grouped by patient for the page patients
                            Subquery<Long> latestSubquery = query.subquery(Long.class);
                            Root<ScheduledProgram> maxRoot = latestSubquery.from(ScheduledProgram.class);
                            latestSubquery.select(cb.max(maxRoot.get(ScheduledProgram_.id)))
                                    .groupBy(maxRoot.get(ScheduledProgram_.patient))
                                    .where(maxRoot.get(ScheduledProgram_.patient).in(ret.getContent()));

                            // return the List of ScheduledProgram objects whose IDs match the subquery
                            return cb.in(root.get(ScheduledProgram_.id)).value(latestSubquery);
                        }
                    })) {
                entityManager.detach(scheduledProgram); // allows us to modify the object without persisting changes
                latestScheduledProgramMap.put(scheduledProgram.getPatient(), scheduledProgram);
                scheduledProgram.setPatient(null); // so no infinite loops during serialization
            }
        }

        // overwrite the scheduledPrograms collection with the latest if there are some found
        if (!latestScheduledProgramMap.isEmpty()) {
            for (Patient patient : ret) {
                Set<ScheduledProgram> scheduledPrograms = new HashSet<>();
                if (latestScheduledProgramMap.containsKey(patient)) {
                    scheduledPrograms.add(latestScheduledProgramMap.get(patient));
                }
                patient.setScheduledPrograms(Collections.unmodifiableSet(scheduledPrograms));
            }
        }

        return ret;
    }

    @Override
    public Collection<PatientOptOutPreference> allPossibleOptOutPreferences() {
        return patientOptOutPreferenceRepository.findAll();
    }
}
