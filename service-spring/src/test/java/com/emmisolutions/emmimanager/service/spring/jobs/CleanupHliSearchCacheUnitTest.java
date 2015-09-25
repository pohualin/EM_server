package com.emmisolutions.emmimanager.service.spring.jobs;

import com.emmisolutions.emmimanager.service.BaseUnitTest;
import com.emmisolutions.emmimanager.service.ProgramService;
import mockit.Mocked;
import mockit.Verifications;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;

/**
 * Unit test for the cleanup hli search job
 */
public class CleanupHliSearchCacheUnitTest extends BaseUnitTest {

    /**
     * Make sure that execute() on the quartz bean calls the hli clean service
     *
     * @param programService      a mock
     * @param scheduler           a mock
     * @param jobExecutionContext a mock
     * @throws JobExecutionException shouldn't happen.. scheduler api
     */
    @Test
    public void happyPath(@Mocked final ProgramService programService,
                          @Mocked final Scheduler scheduler,
                          @Mocked final JobExecutionContext jobExecutionContext) throws JobExecutionException {
        CleanupHliSearchCache svc = new CleanupHliSearchCache();
        svc.programService = programService;
        svc.scheduler = scheduler;
        svc.execute(jobExecutionContext);

        new Verifications() {{
            programService.clearHliCache();
            times = 1;
        }};
    }
}
