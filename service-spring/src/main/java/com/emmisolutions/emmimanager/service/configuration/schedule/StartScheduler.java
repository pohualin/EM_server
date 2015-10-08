package com.emmisolutions.emmimanager.service.configuration.schedule;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Starts the quartz scheduler
 */
@Component
public class StartScheduler implements ApplicationListener<ContextRefreshedEvent> {

    private final Logger LOGGER = LoggerFactory.getLogger(StartScheduler.class);

    @Resource
    Scheduler scheduler;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        try {
            if (!scheduler.isStarted()) {
                scheduler.startDelayed(60);
            }
        } catch (SchedulerException e) {
            LOGGER.error("Could not start scheduler", e);
        }
    }
}
