package com.emmisolutions.emmimanager.persistence.configuration;

import com.emmisolutions.emmimanager.config.Constants;
import com.emmisolutions.emmimanager.persistence.logging.LoggingAspect;
import liquibase.integration.spring.SpringLiquibase;
import org.h2.tools.Server;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jndi.JndiPropertySource;
import org.springframework.jndi.JndiTemplate;
import org.springframework.orm.jpa.JpaDialect;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.PlatformTransactionManager;

import javax.naming.NamingException;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Properties;

import static com.emmisolutions.emmimanager.config.Constants.*;
import static org.hibernate.cfg.AvailableSettings.DIALECT;
import static org.hibernate.cfg.Environment.SHOW_SQL;


/**
 * Configures spring beans necessary for persistence
 */
@Configuration
@EnableAspectJAutoProxy
@ComponentScan(basePackages = {
        "com.emmisolutions.emmimanager.persistence.impl"
})
@EnableJpaRepositories(basePackages = {
        "com.emmisolutions.emmimanager.persistence.repo"
})
@EnableJpaAuditing(auditorAwareRef = "springDataAuditor")
public class PersistenceConfiguration {

    private transient final Logger LOGGER = LoggerFactory.getLogger(PersistenceConfiguration.class);

    @Value("${hibernate.dialect:org.hibernate.dialect.H2Dialect}")
    private String dialect;

    @Value("${hibernate.show_sql:true}")
    private Boolean showSql;

    /**
     * Transaction manager
     *
     * @param emf the factory
     * @return the transaction manager
     */
    @Bean(name = "transactionManager")
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(emf);
        return transactionManager;
    }

    /**
     * Translate exceptions into spring exceptions
     *
     * @return the bean
     */
    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

    /**
     * The factory
     *
     * @param dataSource used by the factory
     * @param jpaDialect the dialect
     * @return the factory
     */
    @Bean(name = "entityManagerFactory")
    @DependsOn("cacheManager")
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean(DataSource dataSource, JpaDialect jpaDialect, Environment env) {
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(dataSource);
        entityManagerFactoryBean.setPersistenceProviderClass(HibernatePersistenceProvider.class);
        entityManagerFactoryBean.setPackagesToScan("com.emmisolutions.emmimanager.model");
        entityManagerFactoryBean.setPersistenceUnitName("EmmiManagerPersistenceUnit");
        entityManagerFactoryBean.setJpaDialect(jpaDialect);
        entityManagerFactoryBean.setJpaProperties(getCommonJpaProperties(env));
        return entityManagerFactoryBean;
    }

    /**
     * The dialect
     *
     * @return jpa dialect
     */
    @Bean
    public JpaDialect getJpaDialect() {
        return new HibernateJpaDialect();
    }

    /**
     * the data auditor
     *
     * @return the auditor
     */
    @Bean(name = "springDataAuditor")
    public AuditorAware<String> getAuditorAware() {
        return new AuditorAware<String>() {
            @Override
            public String getCurrentAuditor() {
                SecurityContext securityContext = SecurityContextHolder.getContext();
                Authentication authentication = securityContext.getAuthentication();
                String userName = null;
                if (authentication != null) {
                    if (authentication.getPrincipal() instanceof UserDetails) {
                        UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
                        userName = springSecurityUser.getUsername();
                    } else if (authentication.getPrincipal() instanceof String) {
                        userName = (String) authentication.getPrincipal();
                    }
                }
                return (userName != null ? userName : Constants.SYSTEM_ACCOUNT);
            }
        };
    }

    /**
     * Liquibase hook
     *
     * @param dataSource to use
     * @param env        in which we are in
     * @return the liquibase bean
     */
    @Bean
    public SpringLiquibase getDbUpdater(DataSource dataSource, Environment env) {
        SpringLiquibase springLiquibase = new SpringLiquibase();
        springLiquibase.setDataSource(dataSource);
        springLiquibase.setChangeLog("classpath:db.changelog-master.xml");
        if (env.acceptsProfiles(SPRING_PROFILE_TEST)) {
            springLiquibase.setContexts("test");
        }
        if (env.acceptsProfiles(SPRING_PROFILE_DEVELOPMENT)) {
            springLiquibase.setContexts("dev");
        }
        if (env.acceptsProfiles(SPRING_PROFILE_QA)) {
            springLiquibase.setContexts("qa");
        }
        return springLiquibase;
    }

    /**
     * The data source
     *
     * @return the datasource
     * @throws NamingException if there isn't a jndi bean
     */
    @Bean
    @Profile({SPRING_PROFILE_JNDI_PERSISTENCE, SPRING_PROFILE_PRODUCTION})
    public DataSource getDataSource() throws NamingException {
        JndiTemplate jndi = new JndiTemplate();
        return jndi.lookup("java:comp/env/jdbc/EmmiManagerDS", DataSource.class);
    }

    /**
     * Add JNDI resolver for all properties.
     *
     * @param environment to add the source to
     * @return the scanner
     */
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyResolver(ConfigurableEnvironment environment) {
        // allow for JNDI properties to be resolved
        JndiPropertySource jndiPropertySource = new JndiPropertySource("jndiProperties");
        environment.getPropertySources().addLast(jndiPropertySource);
        PropertySourcesPlaceholderConfigurer ret = new PropertySourcesPlaceholderConfigurer();
        ret.setPropertySources(environment.getPropertySources());
        return ret;
    }

    /**
     * Datasource for testing
     * @return data source
     */
    @Bean
    @Profile({SPRING_PROFILE_TEST, SPRING_PROFILE_H2})
    public DataSource getTestingDataSource(Environment env) {
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName("org.h2.Driver");
        ds.setUrl("jdbc:h2:mem:EmmiManager;DB_CLOSE_DELAY=-1");
        ds.setUsername("sa");
        ds.setPassword("");
        if (env.acceptsProfiles(SPRING_PROFILE_H2)) {
            try {
                Server server = Server.createTcpServer().start();
                LOGGER.info("In memory database server started and connection is open.");
                LOGGER.info("Connection URL: jdbc:h2:{}/mem:EmmiManager", server.getURL());
            } catch (SQLException e) {
                LOGGER.error("Problem connecting to in memory database", e);
            }
        }
        return ds;
    }

    /**
     * auto logging bean
     * @return the aspect
     */
    @Bean(name = "PersistenceLayerAutoLogger")
    @Profile({SPRING_PROFILE_DEVELOPMENT})
    public LoggingAspect getPersistenceLayerAutoLogger() {
        return new LoggingAspect();
    }


    private Properties getCommonJpaProperties(Environment env) {
        Properties properties = new Properties();
        properties.setProperty(DIALECT, dialect);
        properties.setProperty(SHOW_SQL, env.acceptsProfiles(SPRING_PROFILE_TEST) ? "false" : showSql.toString());
        properties.setProperty("jadira.usertype.autoRegisterUserTypes", "true");
        properties.setProperty("javax.persistence.validation.mode", "ddl, callback");
        properties.setProperty("org.hibernate.envers.audit_table_suffix", "_audit");
        properties.setProperty("org.hibernate.envers.revision_field_name", "revision");
        properties.setProperty("org.hibernate.envers.revision_type_field_name", "revision_type");
        properties.setProperty("hibernate.cache.use_second_level_cache", "true");
        properties.setProperty("hibernate.cache.use_query_cache", "true");
        properties.setProperty("hibernate.generate_statistics", "true");
        properties.setProperty("hibernate.cache.region.factory_class", "com.emmisolutions.emmimanager.persistence.configuration.HazelcastCacheRegionFactory");
        properties.setProperty("hibernate.cache.use_minimal_puts", "true");
        properties.setProperty("hibernate.cache.hazelcast.use_lite_member", "true");
        return properties;
    }


}
