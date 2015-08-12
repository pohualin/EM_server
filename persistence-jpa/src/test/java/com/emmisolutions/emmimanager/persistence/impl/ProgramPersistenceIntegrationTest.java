package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.program.*;
import com.emmisolutions.emmimanager.persistence.BaseIntegrationTest;
import com.emmisolutions.emmimanager.persistence.ProgramPersistence;
import com.emmisolutions.emmimanager.persistence.repo.ProgramRepository;
import org.junit.Test;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Test to ensure programs are loaded properly
 */
public class ProgramPersistenceIntegrationTest extends BaseIntegrationTest {

    @Resource
    ProgramPersistence programPersistence;

    @Resource
    ProgramRepository programRepository;

    /**
     * Make sure the db is hooked up and sample data loads properly
     */
    @Test
    public void programs() {
        assertThat("we can get a page", programPersistence.find(null, null).hasContent(), is(true));
    }

    /**
     * From the test data loaded (RF_EMMI_SPECIALTY_NEW.test.csv), specialty 16 === program 23 and
     * specialty 24 === program 10. Filtering on both of those specialties should find the
     * two programs.
     */
    @Test
    public void findViaSpecialties() {
        assertThat("found programs using filter", programPersistence.find(new ProgramSearchFilter()
                        .addSpecialty(new Specialty(16)).addSpecialty(new Specialty(24)), null),
                hasItems(new Program(23), new Program(10)));
    }

    /**
     * Make sure specialties without IDs are ignored for the search purposes
     */
    @Test
    public void ignoreSpecialtiesWithIds() {
        assertThat("specialty without id isn't added to filter, so all programs are returned",
                programPersistence.find(new ProgramSearchFilter().addSpecialty(new Specialty()), null).hasContent(),
                is(true));

        // hack to add invalid specialty without using the search filter api
        ProgramSearchFilter filter = new ProgramSearchFilter().addSpecialty(new Specialty(333));
        filter.getSpecialties().add(new Specialty());
        assertThat("bad specialty (without id) shouldn't break the search",
                programPersistence.find(filter, null).hasContent(), is(false));
    }

    /**
     * Make sure specialties come back
     */
    @Test
    public void specialties() {
        assertThat("we can get a page", programPersistence.findSpecialties(null).hasContent(), is(true));
    }

    @Test
    public void description() {
        Program p5320 = new Program(5320);
        p5320.setName("first heart program");
        p5320.setBrand(new Brand(1));
        p5320.setType(new Type(1));
        p5320.setSource(new Source(1));
        programRepository.save(p5320);

        assertThat("make sure ordering of found program is based upon HLI ordering",
                programPersistence.find(new ProgramSearchFilter().addTerm("heart").addTerm("repair"),
                        new PageRequest(0, 1)),
                hasItems(new Program(5320)));
    }
}
