package com.emmisolutions.emmimanager.persistence.configuration;

import com.emmisolutions.emmimanager.persistence.logging.LoggingAspect;
import liquibase.integration.spring.SpringLiquibase;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.jadira.usertype.spi.jta.HibernateEntityManagerFactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jndi.JndiPropertySource;
import org.springframework.jndi.JndiTemplate;
import org.springframework.orm.jpa.JpaDialect;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.transaction.PlatformTransactionManager;

import javax.naming.NamingException;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

import static com.emmisolutions.emmimanager.config.Constants.*;
import static org.hibernate.cfg.AvailableSettings.DIALECT;
import static org.hibernate.cfg.Environment.SHOW_SQL;


@Configuration
@EnableJpaAuditing
@EnableAspectJAutoProxy
@ComponentScan(basePackages = {
        "com.emmisolutions.emmimanager.persistence.impl"
})
@EnableJpaRepositories(basePackages = "com.emmisolutions.emmimanager.persistence.repo")
public class PersistenceConfiguration {

    @Bean(name = "transactionManager")
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(emf);
        return transactionManager;
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

    @Bean(name = "entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean(DataSource dataSource, JpaDialect jpaDialect) {
        HibernateEntityManagerFactoryBean entityManagerFactoryBean = new HibernateEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(dataSource);
        entityManagerFactoryBean.setPersistenceProviderClass(HibernatePersistenceProvider.class);
        entityManagerFactoryBean.setJpaDialect(jpaDialect);
        Properties properties = new Properties();
        properties.setProperty(DIALECT, dialect);
        properties.setProperty(SHOW_SQL, showSql.toString());
        properties.setProperty("jadira.usertype.autoRegisterUserTypes", "true");
        properties.setProperty("jadira.usertype.javaZone", "UTC");
        properties.setProperty("jadira.usertype.databaseZone", "UTC");
        entityManagerFactoryBean.setJpaProperties(properties);
        return entityManagerFactoryBean;
    }

    @Value("${hibernate.dialect}")
    String dialect;

    @Value("${hibernate.show_sql}")
    Boolean showSql;

    @Bean
    public JpaDialect getJpaDialect() {
        return new HibernateJpaDialect();
    }

    @Bean
    public SpringLiquibase getSpringLiquibase(DataSource dataSource) {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setDataSource(dataSource);
        liquibase.setChangeLog("classpath:db.changelog-master.xml");
        return liquibase;
    }

    @Bean
    @Profile({SPRING_PROFILE_JNDI_PERSISTENCE, SPRING_PROFILE_PRODUCTION})
    public DataSource getDataSource() throws NamingException {
        JndiTemplate jndi = new JndiTemplate();
        return jndi.lookup("java:comp/env/jdbc/EmmiManagerDS", DataSource.class);
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyResolver(ConfigurableEnvironment environment) {
        // allow for JNDI properties to be resolved
        JndiPropertySource jndiPropertySource = new JndiPropertySource("jndiProperties");
        environment.getPropertySources().addLast(jndiPropertySource);
        if (environment.acceptsProfiles(SPRING_PROFILE_TEST, SPRING_PROFILE_DEVELOPMENT)) {
            // wire up the test profile properties for the H2 DB
            Properties properties = new Properties();
            properties.setProperty(DIALECT, "org.hibernate.dialect.H2Dialect");
            properties.setProperty(SHOW_SQL, "true");
            properties.setProperty("org.hibernate.envers.audit_table_suffix", "_aud");
            properties.setProperty("org.hibernate.envers.revision_field_name", "rev");
            properties.setProperty("org.hibernate.envers.revision_type_field_name", "revtype");
            PropertiesPropertySource pps = new PropertiesPropertySource("testing", properties);
            environment.getPropertySources().addLast(pps);
        }
        PropertySourcesPlaceholderConfigurer ret = new PropertySourcesPlaceholderConfigurer();
        ret.setPropertySources(environment.getPropertySources());
        return ret;
    }

    @Bean
    @Profile({SPRING_PROFILE_TEST, SPRING_PROFILE_DEVELOPMENT})
    public DataSource getTestingDataSource() {
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName("org.h2.Driver");
        ds.setUrl("jdbc:h2:mem:EmmiManager;DB_CLOSE_DELAY=-1");
        ds.setUsername("sa");
        ds.setPassword("");
        return ds;
    }

    @Bean(name = "PersistenceLayerAutoLogger")
    @Profile({SPRING_PROFILE_DEVELOPMENT, SPRING_PROFILE_TEST})
    public LoggingAspect getPersistenceLayerAutoLogger() {
        return new LoggingAspect();
    }


}
