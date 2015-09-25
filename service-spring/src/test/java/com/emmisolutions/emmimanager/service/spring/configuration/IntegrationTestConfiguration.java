package com.emmisolutions.emmimanager.service.spring.configuration;

import com.emmisolutions.emmimanager.service.configuration.schedule.SpringBeanJobFactory;
import com.icegreen.greenmail.spring.GreenMailBean;
import mockit.MockUp;
import mockit.Mocked;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.SchedulerRepository;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.annotation.Resource;

import static com.emmisolutions.emmimanager.service.configuration.schedule.SpringBeanJobFactory.APPLICATION_CONTEXT_KEY;

/**
 * Any beans only necessary for testing
 */
@Configuration
public class IntegrationTestConfiguration {

    /**
     * An in memory email server
     *
     * @return GreenMailBean
     */
    @Bean
    public GreenMailBean greenMailBean() {
        return new GreenMailBean();
    }

    @Bean(name = "quartzScheduler")
    public Scheduler scheduler() throws SchedulerException {
        return StdSchedulerFactory.getDefaultScheduler();
    }

    /**
     * configures the java mail sender to use the green mail
     * server
     *
     * @param javaMailSender to configure
     */
    @Resource
    public void setJavaMailSender(JavaMailSenderImpl javaMailSender) {
        javaMailSender.setHost(greenMailBean().getHostname());
        javaMailSender.setPort(greenMailBean().getGreenMail().getSmtp().getPort());
    }
}
