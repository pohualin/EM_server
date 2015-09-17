package com.emmisolutions.emmimanager.service.spring;

import com.emmisolutions.emmimanager.model.program.Program;
import com.emmisolutions.emmimanager.model.program.ProgramSearchFilter;
import com.emmisolutions.emmimanager.model.program.Specialty;
import com.emmisolutions.emmimanager.service.BaseIntegrationTest;
import com.emmisolutions.emmimanager.service.ProgramService;
import org.junit.Test;
import org.springframework.data.domain.Page;

import javax.annotation.Resource;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

/**
 * Test for the program service
 */
public class ProgramServiceIntegrationTest extends BaseIntegrationTest {

    @Resource
    ProgramService programService;

    /**
     * Ensures the classes are all hooked up
     */
    @Test
    public void findAll() {
        assertThat("we can get a page", programService.find(null, null).hasContent(), is(true));
    }

    /**
     * From the test data loaded (RF_EMMI_SPECIALTY_NEW.test.csv), specialty 16 === program 23 and
     * specialty 24 === program 10. Filtering on both of those specialties should find the
     * two programs.
     */
    @Test
    public void findViaSpecialties(){
        assertThat("found programs using filter", programService.find(new ProgramSearchFilter()
                        .addSpecialty(new Specialty(16)).addSpecialty(new Specialty(24)), null),
                hasItems(new Program(23), new Program(10)));
    }

    /**
     * Searching for specialty that doesn't exist should return zero results
     */
    @Test
    public void notFoundSpecialty(){
        assertThat("not found specialty should return nothing", programService.find(new ProgramSearchFilter()
                        .addSpecialty(new Specialty(22222)), null).hasContent(), is(false));
    }

    /**
     * Make sure specialties without IDs are ignored for the search purposes
     */
    @Test
    public void ignoreSpecialtiesWithIds(){
        assertThat("specialty without id isn't added to filter, so all programs are returned",
                programService.find(new ProgramSearchFilter().addSpecialty(new Specialty()), null).hasContent(),
                is(true));

        // hack to add invalid specialty with not found specialty
        ProgramSearchFilter filter = new ProgramSearchFilter().addSpecialty(new Specialty(333));
        filter.getSpecialties().add(new Specialty());
        assertThat("bad specialty (without id) shouldn't break the search",
                programService.find(filter, null).hasContent(), is(false));
    }

    /**
     * Make sure specialties come back
     */
    @Test
    public void specialties(){
        assertThat("we can get a page", programService.findSpecialties(null).hasContent(), is(true));
    }

    /**
     * Searching by a term should call HLI and narrow the list of programs properly
     */
    @Test
    public void searchByTerm() {
        assertThat("heart program should match term 'heart'", programService.find(new ProgramSearchFilter().addTerm("heart"), null),
                hasItem(new Program(10)));

        Page<Program> programPage = programService.find(new ProgramSearchFilter().addTerm("soul"), null);

        assertThat("no programs should match term 'soul'", programPage,
                not(hasItem(new Program(10))));
    }

    /**
     * Using both specialty and term should narrow even further
     */
    @Test
    public void searchByTermAndSpecialty() {
        assertThat("we can find programs by term and specialty", programService.find(
                        new ProgramSearchFilter().addTerm("heart").addSpecialty(new Specialty(23)), null),
                hasItem(new Program(10)));

        assertThat("term matches but specialty doesn't.. should yield no results", programService.find(
                        new ProgramSearchFilter().addTerm("heart").addSpecialty(new Specialty(10)), null),
                not(hasItem(new Program(10))));
    }

    /**
     * Ensure that the hli cache clear method works.. no
     */
    @Test
    public void callCleanup() {
        assertThat("cache clean works", programService.clearHliCache() >= 0, is(true));
    }

}
