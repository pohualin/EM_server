package com.emmisolutions.emmimanager.persistence.impl.specification;

import com.emmisolutions.emmimanager.model.program.Program;
import com.emmisolutions.emmimanager.model.program.ProgramSearchFilter;
import com.emmisolutions.emmimanager.model.program.ProgramSpecialty;
import com.emmisolutions.emmimanager.model.program.Specialty;
import com.emmisolutions.emmimanager.model.program.hli.HliProgram;
import com.emmisolutions.emmimanager.persistence.repo.HliSearchRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.hibernate.cfg.AvailableSettings.DIALECT;

/**
 * Specifications holder for Program objects
 */
@Component
public class ProgramSpecifications {

    private static ThreadLocal<Boolean> performedHliSearch = new ThreadLocal<>();
    @Resource
    HliSearchRepository hliSearchRepository;
    @PersistenceContext
    EntityManager entityManager;
    @Value("${hibernate.jdbc.batch_size:50}")
    private int batchSize;

    /**
     * Adds an OR clause for each specialty in the filter. This ensures that both
     * the Specialty and ProgramSpecialty are active
     *
     * @param filter containing Collection of 'active' specialties to look for
     * @return a Specification for the Program
     */
    public Specification<Program> hasSpecialties(final ProgramSearchFilter filter) {
        return new Specification<Program>() {
            @Override
            public Predicate toPredicate(Root<Program> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if (filter != null && !CollectionUtils.isEmpty(filter.getSpecialties())) {
                    List<Predicate> predicates = new ArrayList<>();
                    for (Specialty specialty : filter.getSpecialties()) {
                        if (specialty.getId() != null) {
                            SetJoin<Program, ProgramSpecialty> join = root.join(Program_.programSpecialty);
                            predicates.add(
                                    cb.and(
                                            cb.isTrue(join.get(ProgramSpecialty_.active)),
                                            cb.and(cb.equal(join.get(ProgramSpecialty_.specialty), specialty),
                                                    cb.isTrue(join.join(ProgramSpecialty_.specialty).get(Specialty_.active)))
                                    )
                            );
                        }
                    }
                    return cb.or(predicates.toArray(new Predicate[predicates.size()]));
                }
                return null;
            }
        };
    }

    /**
     * Looks up program IDs from HLI and then uses those to narrow the program search
     *
     * @param filter that contains terms
     * @return a specification for program narrowing using ids found from terms
     * or null if there are no terms
     */
    public Specification<Program> matchesTerms(final ProgramSearchFilter filter) {
        return new Specification<Program>() {
            @Override
            public Predicate toPredicate(Root<Program> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if (filter != null && !CollectionUtils.isEmpty(filter.getTerms())) {

                    // only load HLI searches one time per thread
                    if (!Boolean.TRUE.equals(performedHliSearch.get())) {

                        performedHliSearch.set(Boolean.TRUE);

                        // search HLI and get full set
                        Set<HliProgram> hliPrograms = hliSearchRepository.find(filter);

                        // create temporary table (even if there are no programs found)
                        createTempTable();

                        if (!CollectionUtils.isEmpty(hliPrograms)) {
                            // insert full set into temporary table
                            int i = 0;
                            for (HliProgram hliProgram : hliPrograms) {
                                entityManager.persist(hliProgram);
                                i++;
                                if (i % batchSize == 0) {
                                    // Flush a batch of inserts and release memory.
                                    entityManager.flush();
                                    entityManager.clear();
                                }
                            }
                        }
                    }
                    // inner join to the search table to only allow HLI codes
                    root.join(Program_.hliProgram, JoinType.INNER);
                }
                return null;
            }
        };
    }

    private void createTempTable() {
        String dialect = (String) entityManager.getEntityManagerFactory().getProperties().get(DIALECT);
        entityManager.createNamedQuery(dialect).executeUpdate();
    }

    public void resetThread() {
        performedHliSearch.remove();
    }
}
