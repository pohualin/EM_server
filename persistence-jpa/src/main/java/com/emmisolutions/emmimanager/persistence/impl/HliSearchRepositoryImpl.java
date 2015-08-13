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
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository for HLI searches
 */
@Repository
public class HliSearchRepositoryImpl implements HliSearchRepository {

    @Resource
    HliSearchRequestRepository hliSearchRequestRepository;
    @Resource
    ProgramRepository programRepository;
    @Value("${hli.search.url:http://192.168.101.65:8080/hliservice/search}")
    private String baseUrl;

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
        if (hliSearchResponseEntity.getStatusCode().is2xxSuccessful()) {
            List<HliSearchResponse> searchResponses = null;

            for (HliProgramJson aProgram : hliSearchResponseEntity.getBody()) {
                if (StringUtils.isNumeric(aProgram.getCode())) {
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
