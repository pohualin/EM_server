package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.program.ProgramSearchFilter;
import com.emmisolutions.emmimanager.persistence.BaseIntegrationTest;
import com.emmisolutions.emmimanager.persistence.repo.HliSearchRepository;
import org.junit.Test;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

/**
 * HLI search testing
 */
public class HliSearchRepositoryPersistenceIntegrationTest extends BaseIntegrationTest {

    @Resource
    HliSearchRepository hliSearchRepository;

    /**
     * A vaild HLI search should return results
     */
    @Test
    public void valid() {
        assertThat("should find a program",
                CollectionUtils.isEmpty(
                        hliSearchRepository.find(new ProgramSearchFilter()
                                        .addTerm("mouth")
                                        .addTerm("cetirizine/pseudoephedrine")
                        )),
                is(false)
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
