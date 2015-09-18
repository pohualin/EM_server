package com.emmisolutions.emmimanager.service.configuration.schedule;


import org.quartz.SchedulerContext;
import org.quartz.spi.TriggerFiredBundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.context.ApplicationContext;

/**
 * Monkey patch to allow job bean creation to come from existing spring context
 * instead of new instantiation.
 */
public class SpringBeanJobFactory
        extends org.springframework.scheduling.quartz.SpringBeanJobFactory {

    public static final String APPLICATION_CONTEXT_KEY = "applicationContext";
    private static final Logger LOGGER = LoggerFactory.getLogger(SpringBeanJobFactory.class);
    private String[] ignoredUnknownProperties;
    private SchedulerContext schedulerContext;

    @Override
    public void setIgnoredUnknownProperties(String[] ignoredUnknownProperties) {
        super.setIgnoredUnknownProperties(ignoredUnknownProperties);
        this.ignoredUnknownProperties = ignoredUnknownProperties;
    }

    @Override
    public void setSchedulerContext(SchedulerContext schedulerContext) {
        super.setSchedulerContext(schedulerContext);
        this.schedulerContext = schedulerContext;
    }

    /**
     * An implementation of SpringBeanJobFactory that retrieves the bean from
     * the Spring context so that autowiring and transactions work
     * <p/>
     * This method is overridden.
     *
     * @see org.springframework.scheduling.quartz.SpringBeanJobFactory#createJobInstance(org.quartz.spi.TriggerFiredBundle)
     */
    @Override
    protected Object createJobInstance(TriggerFiredBundle bundle) throws Exception {
        ApplicationContext ctx = ((ApplicationContext) schedulerContext.get(APPLICATION_CONTEXT_KEY));

        Object job;
        try {
            job = ctx.getBean(bundle.getJobDetail().getKey().getName());
        } catch (BeansException be) {
            LOGGER.error("Error instantiating job details. " +
                            "Did you forget to name your bean definition? E.g. @Service(\"{}\") or @Component(\"{}\")?",
                    bundle.getJobDetail().getKey().getName(), bundle.getJobDetail().getKey().getName());
            throw be;
        }
        BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(job);
        if (isEligibleForPropertyPopulation(bw.getWrappedInstance())) {
            MutablePropertyValues pvs = new MutablePropertyValues();
            if (this.schedulerContext != null) {
                pvs.addPropertyValues(this.schedulerContext);
            }
            pvs.addPropertyValues(bundle.getJobDetail().getJobDataMap());
            pvs.addPropertyValues(bundle.getTrigger().getJobDataMap());
            if (this.ignoredUnknownProperties != null) {
                for (String propName : this.ignoredUnknownProperties) {
                    if (pvs.contains(propName) && !bw.isWritableProperty(propName)) {
                        pvs.removePropertyValue(propName);
                    }
                }
                bw.setPropertyValues(pvs);
            } else {
                bw.setPropertyValues(pvs, true);
            }
        }
        return job;
    }

}

