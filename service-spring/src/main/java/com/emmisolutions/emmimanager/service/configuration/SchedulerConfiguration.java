package com.emmisolutions.emmimanager.service.configuration;

import com.emmisolutions.emmimanager.service.configuration.schedule.SpringBeanJobFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.Properties;

import static com.emmisolutions.emmimanager.service.configuration.schedule.SpringBeanJobFactory.APPLICATION_CONTEXT_KEY;
import static org.quartz.impl.StdSchedulerFactory.*;

/**
 * Scheduling Configuration
 */
@Configuration
@Profile("!test")
@EnableScheduling
@DependsOn("dbUpdater")
public class SchedulerConfiguration implements SchedulingConfigurer {

    @Value("${scheduler.poolSize:2}")
    private Integer poolSize;
    @Value("${org.quartz.jobStore.class:org.springframework.scheduling.quartz.LocalDataSourceJobStore}")
    private String jobStoreClass;
    @Value("${org.quartz.jobStore.tablePrefix:quartz.qrtz_}")
    private String tablePrefix;
    @Value("${org.quartz.jobStore.driverDelegateClass:org.quartz.impl.jdbcjobstore.HSQLDBDelegate}")
    private String driverDelegateClass;
    @Value("${org.quartz.jobStore.isClustered:true}")
    private String isClustered;
    @Value("${org.quartz.jobStore.useProperties:true}")
    private String useProperties;
    @Value("${org.quartz.jobStore.lockHandler.class:org.quartz.impl.jdbcjobstore.UpdateLockRowSemaphore}")
    private String lockHandlerClass;
    @Value("${org.quartz.jobStore.clusterCheckinInterval:20000}")
    private String clusterCheckinInterval;
    @Value("${org.quartz.scheduler.instanceName:em2_clustered_scheduler}")
    private String instanceName;
    @Value("${org.quartz.scheduler.instanceId:AUTO}")
    private String instanceId;
    @Value("${org.quartz.scheduler.skipUpdateCheck:true}")
    private String skipUpdateCheck;
    @Resource(name = "dataSource")
    private DataSource dataSource;

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setTaskScheduler(taskScheduler());
    }

    /**
     * Creates the JVM local task scheduler
     *
     * @return a thread pool task scheduler
     */
    @Bean
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(poolSize);
        threadPoolTaskScheduler.setThreadNamePrefix("emmiManager-scheduler-");
        return threadPoolTaskScheduler;
    }

    /**
     * Creates a cluster aware, cross process/vm scheduler
     *
     * @param txManager the transaction manager
     * @return the Scheduler
     */
    @Bean(name = "quartzScheduler")
    @DependsOn("dbUpdater")
    public SchedulerFactoryBean persistentScheduler(PlatformTransactionManager txManager) {
        SchedulerFactoryBean scheduler = new SchedulerFactoryBean();
        scheduler.setDataSource(dataSource);
        scheduler.setTransactionManager(txManager);
        scheduler.setJobFactory(new SpringBeanJobFactory());
        scheduler.setAutoStartup(false);
        scheduler.setApplicationContextSchedulerContextKey(APPLICATION_CONTEXT_KEY);
        scheduler.setQuartzProperties(quartzProperties());
        scheduler.setWaitForJobsToCompleteOnShutdown(true);
        return scheduler;
    }

    private Properties quartzProperties() {
        Properties properties = new Properties();
        properties.put(PROP_JOB_STORE_CLASS, jobStoreClass);
        properties.put(PROP_JOB_STORE_USE_PROP, useProperties);
        properties.put(PROP_JOB_STORE_LOCK_HANDLER_CLASS, lockHandlerClass);
        properties.put(PROP_JOB_STORE_PREFIX + "." + PROP_TABLE_PREFIX, tablePrefix);
        properties.put(PROP_JOB_STORE_PREFIX + ".driverDelegateClass", driverDelegateClass);
        properties.put(PROP_JOB_STORE_PREFIX + ".isClustered", isClustered);
        properties.put(PROP_JOB_STORE_PREFIX + ".clusterCheckinInterval", clusterCheckinInterval);
        properties.put(PROP_SCHED_INSTANCE_NAME, instanceName);
        properties.put(PROP_SCHED_INSTANCE_ID, instanceId);
        properties.put(PROP_SCHED_SKIP_UPDATE_CHECK, skipUpdateCheck);
        return properties;
    }

}
