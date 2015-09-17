package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.program.Program;
import com.emmisolutions.emmimanager.model.program.ProgramSearchFilter;
import com.emmisolutions.emmimanager.model.program.hli.HliSearchRequest;
import com.emmisolutions.emmimanager.model.program.hli.HliSearchRequest_;
import com.emmisolutions.emmimanager.model.program.hli.HliSearchResponse;
import com.emmisolutions.emmimanager.model.program.hli.remote.HliProgramJson;
import com.emmisolutions.emmimanager.persistence.repo.HliSearchRepository;
import com.emmisolutions.emmimanager.persistence.repo.HliSearchRequestRepository;
import com.emmisolutions.emmimanager.persistence.repo.ProgramRepository;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.joda.time.DateTimeZone.UTC;

/**
 * Repository for HLI searches
 */
@Repository
public class HliSearchRepositoryImpl implements HliSearchRepository {

    private transient final Logger LOGGER = LoggerFactory.getLogger(HliSearchRepositoryImpl.class);

    @Resource
    HliSearchRequestRepository hliSearchRequestRepository;
    @Resource
    ProgramRepository programRepository;
    @PersistenceContext
    EntityManager entityManager;
    @Value("${hli.search.url:http://192.168.101.65:8080/hliservice/search}")
    private String baseUrl;
    @Value("${hli.cache.oldest:900000}")
    private int oldestSearchRequest;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public HliSearchRequest find(ProgramSearchFilter filter) {
        if (filter == null || CollectionUtils.isEmpty(filter.getTerms())) {
            return null;
        }
        HliSearchRequest ret;
        final String query = StringUtils.join(filter.getTerms(), " ");
        Page<HliSearchRequest> hliSearchRequests = hliSearchRequestRepository.findAll(new Specification<HliSearchRequest>() {
            @Override
            public Predicate toPredicate(Root<HliSearchRequest> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                return cb.equal(root.get(HliSearchRequest_.searchHash), query);
            }
        }, new PageRequest(0, 1, new Sort(Sort.Direction.DESC, "createdDate")));
        if (hliSearchRequests.hasContent()) {
            ret = hliSearchRequests.iterator().next();
        } else {
            ret = create(query);
        }
        return ret;
    }

    @Override
    public int cacheClean() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaDelete<HliSearchRequest> delete = criteriaBuilder.createCriteriaDelete(HliSearchRequest.class);
        Root<HliSearchRequest> hliSearchRequest = delete.from(HliSearchRequest.class);
        delete.where(criteriaBuilder.lessThan(hliSearchRequest.get("createdDate").as(DateTime.class),
                DateTime.now(UTC).minusMillis(oldestSearchRequest)));
        Query query = entityManager.createQuery(delete);
        return query.executeUpdate();
    }

    private HliSearchRequest create(String query) {

        HliSearchRequest hliSearchRequest = new HliSearchRequest();
        hliSearchRequest.setSearchHash(query);
        hliSearchRequest.setCreatedDate(DateTime.now(DateTimeZone.UTC));

        RestTemplate restTemplate = new RestTemplate();

        // construct the url based upon the search terms
        UriComponentsBuilder searchUrl =
                UriComponentsBuilder.fromHttpUrl(baseUrl)
                        .pathSegment(query);

        // call HLI
        ResponseEntity<List<HliProgramJson>> hliSearchResponseEntity =
                restTemplate.exchange(searchUrl.toUriString(),
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<HliProgramJson>>() {
                        });
        int weight = 0;

        // successful response, create db cache based upon responses
        if (hliSearchResponseEntity.getStatusCode().is2xxSuccessful()) {
            List<HliSearchResponse> searchResponses = null;

            // load all of the ids into jpa session
            Set<Integer> ids = new HashSet<>();
            for (HliProgramJson hliProgramJson : hliSearchResponseEntity.getBody()) {
                if (StringUtils.isNumeric(hliProgramJson.getCode())) {
                    ids.add(new Integer(hliProgramJson.getCode()));
                }
            }
            programRepository.findAll(ids);

            // create search response objects from the hli response
            for (HliProgramJson aProgram : hliSearchResponseEntity.getBody()) {
                if (StringUtils.isNumeric(aProgram.getCode())) {
                    // this won't call the db because of the L1 cache from the previous call
                    Program program = programRepository.findOne(new Integer(aProgram.getCode()));
                    if (program != null) {
                        if (searchResponses == null) {
                            searchResponses = new ArrayList<>();
                        }
                        HliSearchResponse hliSearchResponse = new HliSearchResponse();
                        hliSearchResponse.setWeight(weight);
                        hliSearchResponse.setHliSearchRequest(hliSearchRequest);
                        hliSearchResponse.setProgram(program);
                        searchResponses.add(hliSearchResponse);
                        weight++;
                    }
                }
            }
            hliSearchRequest.setPrograms(searchResponses);
        }

        return hliSearchRequestRepository.save(hliSearchRequest);
    }


}
