package com.emmisolutions.emmimanager.persistence.impl;

import com.emmisolutions.emmimanager.model.program.Program;
import com.emmisolutions.emmimanager.model.program.ProgramSearchFilter;
import com.emmisolutions.emmimanager.model.program.Specialty;
import com.emmisolutions.emmimanager.persistence.BaseIntegrationTest;
import com.emmisolutions.emmimanager.persistence.ProgramPersistence;
import org.junit.Test;

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
    public void findViaSpecialties(){
        assertThat("found programs using filter", programPersistence.find(new ProgramSearchFilter()
                        .addSpecialty(new Specialty(16)).addSpecialty(new Specialty(24)), null),
                hasItems(new Program(23), new Program(10)));
    }

    /**
     * Make sure specialties without IDs are ignored for the search purposes
     */
    @Test
    public void ignoreSpecialtiesWithIds(){
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
    public void specialties(){
        assertThat("we can get a page", programPersistence.findSpecialties(null).hasContent(), is(true));
    }
}
