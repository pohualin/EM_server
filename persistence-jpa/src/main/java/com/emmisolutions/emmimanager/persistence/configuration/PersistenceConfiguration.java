package com.emmisolutions.emmimanager.persistence.configuration;

import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jndi.JndiPropertySource;
import org.springframework.jndi.JndiTemplate;
import org.springframework.orm.jpa.JpaDialect;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.naming.NamingException;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

import static org.hibernate.cfg.Environment.DIALECT;
import static org.hibernate.cfg.Environment.SHOW_SQL;


@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = "com.emmisolutions.emmimanager.persistence.impl")
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
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean(DataSource dataSource, JpaDialect dialect) {
        HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
        hibernateJpaVendorAdapter.setDatabase(Database.H2);
        hibernateJpaVendorAdapter.setShowSql(true);
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setJpaVendorAdapter(hibernateJpaVendorAdapter);
        em.setJpaDialect(dialect);
        em.setJpaProperties(additionalProperties());
        return em;
    }

    @Value("${hibernate.dialect}")
    String dialect;

    @Value("${hibernate.show_sql}")
    String showSql;

    Properties additionalProperties() {
        Properties properties = new Properties();
        properties.setProperty(DIALECT, dialect);
        properties.setProperty(SHOW_SQL, showSql);
        return properties;
    }

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
    @Profile("jndi")
    public DataSource getDataSource() throws NamingException {
        JndiTemplate jndi = new JndiTemplate();
        return jndi.lookup("java:comp/env/jdbc/EmmiManagerDS", DataSource.class);
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyResolver(ConfigurableEnvironment environment) {
        // allow for JNDI properties to be resolved
        JndiPropertySource jndiPropertySource = new JndiPropertySource("jndiProperties");
        environment.getPropertySources().addLast(jndiPropertySource);
        if (environment.acceptsProfiles("test")) {
            // wire up the test profile properties for the H2 DB
            Properties properties = new Properties();
            properties.setProperty(DIALECT, "org.hibernate.dialect.H2Dialect");
            properties.setProperty(SHOW_SQL, "true");
            PropertiesPropertySource pps = new PropertiesPropertySource("testing", properties);
            environment.getPropertySources().addLast(pps);
        }
        PropertySourcesPlaceholderConfigurer ret = new PropertySourcesPlaceholderConfigurer();
        ret.setPropertySources(environment.getPropertySources());
        return ret;
    }

    @Bean
    @Profile("test")
    public DataSource getTestingDataSource() {
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName("org.h2.Driver");
        ds.setUrl("jdbc:h2:mem:EmmiManager;DB_CLOSE_DELAY=-1");
        ds.setUsername("sa");
        ds.setPassword("");
        return ds;
    }

}
