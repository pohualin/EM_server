package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.program.*;
import com.emmisolutions.emmimanager.model.program.hli.HliSearchRequest;
import com.emmisolutions.emmimanager.persistence.BaseIntegrationTest;
import com.emmisolutions.emmimanager.persistence.repo.HliSearchRepository;
import com.emmisolutions.emmimanager.persistence.repo.ProgramRepository;
import org.junit.Test;

import javax.annotation.Resource;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

/**
 * HLI search testing
 */
public class HliSearchRepositoryPersistenceIntegrationTest extends BaseIntegrationTest {

    @Resource
    HliSearchRepository hliSearchRepository;

    @Resource
    ProgramRepository programRepository;

    /**
     * A vaild HLI search should return results
     */
    @Test
    public void valid() {

        Program p8752 = new Program(8752);
        p8752.setName("so we can have a program in the system for the result");
        p8752.setBrand(new Brand(1));
        p8752.setType(new Type(1));
        p8752.setSource(new Source(1));
        programRepository.save(p8752);

        HliSearchRequest hliSearchRequest = hliSearchRepository.find(new ProgramSearchFilter()
                .addTerm("ACETAMINOPHEN")
                .addTerm("DIPHENHYDRAMINE")
                .addTerm("PHENYLEPHRINE"));

        assertThat("should create a search request", hliSearchRequest, is(notNullValue()));

        assertThat("find the same search request", hliSearchRepository.find(new ProgramSearchFilter()
                        .addTerm("ACETAMINOPHEN")
                        .addTerm("DIPHENHYDRAMINE")
                        .addTerm("PHENYLEPHRINE")),
                is(hliSearchRequest)
        );
    }

    /**
     * An empty search should return empty and not error out
     */
    @Test
    public void empty() {
        assertThat("null returns empty", hliSearchRepository.find(null), is(nullValue()));
        assertThat("no terms returns empty",
                hliSearchRepository.find(new ProgramSearchFilter()), is(nullValue()));
    }
}
