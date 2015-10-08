package com.emmisolutions.emmimanager.persistence.impl.specification;

import com.emmisolutions.emmimanager.model.Client_;
import com.emmisolutions.emmimanager.model.configuration.ClientProgramContentInclusion;
import com.emmisolutions.emmimanager.model.program.*;
import com.emmisolutions.emmimanager.model.program.hli.HliSearchRequest;
import com.emmisolutions.emmimanager.model.program.hli.HliSearchResponse;
import com.emmisolutions.emmimanager.model.program.hli.HliSearchResponse_;
import com.emmisolutions.emmimanager.persistence.repo.HliSearchRepository;
import com.emmisolutions.emmimanager.model.configuration.ClientProgramContentInclusion_;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.persistence.criteria.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Specifications holder for Program objects
 */
@Component
public class ProgramSpecifications {

    @Resource
    private HliSearchRepository hliSearchRepository;


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
     * Adds an OR clause for the client in the filter. This ensures that 
     * the Program has not associated to the Client as Inclusion before
     * @param filter containing Client to search for Program
     * @return a Specification for the Program
     */
    public Specification<Program> clientProgramInclusion(final ProgramSearchFilter filter) {

    	return new Specification<Program>() {
             @Override
             public Predicate toPredicate(Root<Program> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
            	 
                 if (filter.client() != null && filter != null) {
                     SetJoin<Program, ClientProgramContentInclusion> join = root.join(Program_.programClientInclusion);
                	return cb.and(
                           cb.equal(join.get(ClientProgramContentInclusion_.client).get(Client_.id),
                        		   filter.client().getId()),
                           cb.notEqual(join.get(ClientProgramContentInclusion_.program).get(Program_.id),
                        		   Program_.id));
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

                    // retrieve (or search HLI and create) the persistent search request
                    HliSearchRequest searchRequest = hliSearchRepository.find(filter);

                    // find all programs (on a search response) from the search request
                    SetJoin<Program, HliSearchResponse> hliProgramJoin = root.join(Program_.hliProgram, JoinType.INNER);
                    hliProgramJoin.alias("hliProgram_springDataOrderBy"); // use the inner join for order by
                    return cb.equal(hliProgramJoin.get(HliSearchResponse_.hliSearchRequest), searchRequest);
                }
                return null;
            }
        };
    }

}
