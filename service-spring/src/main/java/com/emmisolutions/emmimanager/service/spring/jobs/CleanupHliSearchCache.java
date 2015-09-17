package com.emmisolutions.emmimanager.service.spring.jobs;

import com.emmisolutions.emmimanager.service.ProgramService;
import com.emmisolutions.emmimanager.service.schedule.AllJobs;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import static com.emmisolutions.emmimanager.service.schedule.AllJobs.CLEANUP_HLI_SEARCH_CACHE_JOB_BEAN_NAME;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.JobKey.jobKey;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * Schedules and executes a job to clean the hli search cache.
 */
@Profile("!test")
@Component(CLEANUP_HLI_SEARCH_CACHE_JOB_BEAN_NAME)
@DisallowConcurrentExecution
public class CleanupHliSearchCache extends QuartzJobBean implements AllJobs {

    private static final transient Logger LOGGER = LoggerFactory.getLogger(CleanupHliSearchCache.class);
    @Resource
    protected PlatformTransactionManager txManager;
    @Resource(name = "quartzScheduler")
    Scheduler scheduler;
    @Resource
    ProgramService programService;
    @Value("${hli.cache.cleanup.job.interval:900000}")
    private long cacheCleanupJobInterval;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        programService.clearHliCache();
    }

    /**
     * Schedules the search clean job if one has not already been scheduled
     */
    @Async
    @PostConstruct
    public void schedule() throws SchedulerException {
        LOGGER.debug("Ensure HLI Cleanup Job Active...");
        if (!scheduler.checkExists(jobKey(
                CLEANUP_HLI_SEARCH_CACHE_JOB_BEAN_NAME, HOUSEKEEPING_GROUP))) {
            LOGGER.debug("Scheduling HLI Cleanup Job");
            scheduler.scheduleJob(
                    newJob(CleanupHliSearchCache.class)
                            .withIdentity(CLEANUP_HLI_SEARCH_CACHE_JOB_BEAN_NAME,
                                    HOUSEKEEPING_GROUP)
                            .build(),
                    newTrigger()
                            .withIdentity(CLEANUP_HLI_SEARCH_CACHE_JOB_BEAN_NAME,
                                    HOUSEKEEPING_GROUP)
                            .startNow()
                            .withSchedule(simpleSchedule()
                                    .repeatForever()
                                    .withIntervalInMilliseconds(cacheCleanupJobInterval))
                            .build());
        }
    }

}
