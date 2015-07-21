package com.emmisolutions.emmimanager.service.configuration;

import org.springframework.context.annotation.*;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

import javax.annotation.Resource;
import javax.persistence.EntityManagerFactory;

/**
 * The service layer spring configuration
 */
@Configuration
@EnableTransactionManagement(mode = AdviceMode.ASPECTJ)
@EnableAspectJAutoProxy()
@ComponentScan(basePackages = {
        "com.emmisolutions.emmimanager.salesforce.configuration",
        "com.emmisolutions.emmimanager.persistence.configuration",
        "com.emmisolutions.emmimanager.service"})
public class ServiceConfiguration implements TransactionManagementConfigurer {

    @Resource
    EntityManagerFactory entityManagerFactory;

    /**
     * Transaction manager
     *
     * @return the transaction manager
     */
    @Bean(name = "transactionManager")
    public PlatformTransactionManager transactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }

    @Override
    public PlatformTransactionManager annotationDrivenTransactionManager() {
        return transactionManager();
    }
}
