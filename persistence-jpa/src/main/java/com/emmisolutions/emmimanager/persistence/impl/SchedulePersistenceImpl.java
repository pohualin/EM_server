package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.Patient;
import com.emmisolutions.emmimanager.model.Team_;
import com.emmisolutions.emmimanager.model.schedule.ScheduledProgram;
import com.emmisolutions.emmimanager.model.schedule.ScheduledProgram_;
import com.emmisolutions.emmimanager.persistence.SchedulePersistence;
import com.emmisolutions.emmimanager.persistence.repo.ScheduledProgramRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * Schedule persistence repository
 */
@Repository
public class SchedulePersistenceImpl implements SchedulePersistence {

    @Resource
    ScheduledProgramRepository scheduledProgramRepository;

    @Override
    public boolean isAccessCodeUnique(String toCheck) {
        return scheduledProgramRepository.findFirstByAccessCodeEquals(toCheck) == null;
    }

    @Override
    public ScheduledProgram save(ScheduledProgram toSave) {
        return scheduledProgramRepository.save(toSave);
    }

    @Override
    public ScheduledProgram reload(final ScheduledProgram scheduledProgram) {
        if (scheduledProgram == null || scheduledProgram.getId() == null){
            return null;
        }
        return scheduledProgramRepository.findOne(new Specification<ScheduledProgram>() {
            @Override
            public Predicate toPredicate(Root<ScheduledProgram> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                predicates.add(cb.equal(root.get(ScheduledProgram_.id), scheduledProgram.getId()));
                if (scheduledProgram.getTeam() != null){
                    // if there's a team make sure the schedule is for that team
                    predicates.add(
                            cb.equal(root.join(ScheduledProgram_.team)
                                    .get(Team_.id), scheduledProgram.getTeam().getId())
                    );
                }
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        });
    }

    @Override
    public Page<ScheduledProgram> findAllByPatient(Patient patient, Pageable page){
        if (patient == null || patient.getId() == null){
            return null;
        }
        if (page == null) {
            // default pagination request if none
            page = new PageRequest(0, 50, Sort.Direction.ASC, "id");
        }
        return scheduledProgramRepository.findAllByPatient(patient, page);
    }
}
