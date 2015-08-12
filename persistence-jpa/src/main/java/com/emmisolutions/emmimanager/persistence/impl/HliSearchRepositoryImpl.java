package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.program.ProgramSearchFilter;
import com.emmisolutions.emmimanager.model.program.hli.HliProgram;
import com.emmisolutions.emmimanager.persistence.repo.HliSearchRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Repository for HLI searches
 */
@Repository
public class HliSearchRepositoryImpl implements HliSearchRepository {

    @Value("${hli.search.url:http://192.168.101.65:8080/hliservice/search}")
    private String baseUrl;

    @Cacheable(value = "hliSearch", key = "#p0.terms", condition = "#p0 != null && #p0.terms.size() > 0")
    public List<HliProgram> find(ProgramSearchFilter filter) {
        if (filter == null || CollectionUtils.isEmpty(filter.getTerms())) {
            return null;
        }
        RestTemplate restTemplate = new RestTemplate();

        // construct the url based upon the search terms
        UriComponentsBuilder searchUrl =
                UriComponentsBuilder.fromHttpUrl(baseUrl)
                        .pathSegment(StringUtils.join(filter.getTerms(), " "));

        // call HLI
        ResponseEntity<List<HliProgram>> hliSearchResponseEntity =
                restTemplate.exchange(searchUrl.toUriString(),
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<HliProgram>>() {
                        });
        int order = 0;
        if (hliSearchResponseEntity.getStatusCode().is2xxSuccessful()) {
            List<HliProgram> ret = null;
            for (HliProgram aProgram : hliSearchResponseEntity.getBody()) {
                if (StringUtils.isNumeric(aProgram.getCode())) {
                    if (ret == null) {
                        ret = new ArrayList<>();
                    }
                    aProgram.setOrder(order);
                    ret.add(aProgram);
                    order++;
                }
            }
            return ret;
        }
        return null;
    }


}
