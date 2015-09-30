package com.emmisolutions.emmimanager.persistence.configuration;

import com.emmisolutions.emmimanager.model.user.AnonymousUser;
import com.emmisolutions.emmimanager.model.user.User;
import com.emmisolutions.emmimanager.persistence.logging.LoggingAspect;
import liquibase.integration.spring.SpringLiquibase;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
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
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.jndi.JndiPropertySource;
import org.springframework.jndi.JndiTemplate;
import org.springframework.orm.jpa.JpaDialect;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.PlatformTransactionManager;

import javax.naming.NamingException;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Properties;
import java.util.TimeZone;

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

    @Value("${hibernate.jdbc.batch_size:50}")
    private String batchSize;

    @Value("${hibernate.show_sql:true}")
    private Boolean showSql;

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
     * @param jpaDialect the dialect
     * @param env        the environment
     * @return the factory
     */
    @Bean(name = "entityManagerFactory")
    @DependsOn("cacheManager")
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean(JpaDialect jpaDialect, Environment env)
            throws NamingException {
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        if (env.acceptsProfiles(SPRING_PROFILE_JNDI_PERSISTENCE, SPRING_PROFILE_PRODUCTION)) {
            entityManagerFactoryBean.setDataSource(getDataSource());
        } else {
            entityManagerFactoryBean.setDataSource(getTestingDataSource(env));
        }
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
    public AuditorAware<User> getAuditorAware() {
        return new AuditorAware<User>() {

            @Override
            public User getCurrentAuditor() {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                if (authentication == null || !authentication.isAuthenticated() ||
                        authentication.getPrincipal() instanceof AnonymousUser) {
                    return null;
                }
                return (User) authentication.getPrincipal();
            }
        };
    }

    /**
     * Liquibase hook
     *
     * @param env in which we are in
     * @return the liquibase bean
     */
    @Bean(name = "dbUpdater")
    public SpringLiquibase getDbUpdater(Environment env) throws NamingException {
        SpringLiquibase springLiquibase = new SpringLiquibase();
        if (env.acceptsProfiles(SPRING_PROFILE_JNDI_PERSISTENCE, SPRING_PROFILE_PRODUCTION)) {
            springLiquibase.setDataSource(getDdlDataSource());
        } else {
            springLiquibase.setDataSource(getTestingDataSource(env));
        }
        springLiquibase.setChangeLog("classpath:db.changelog-master.xml");
        // add all active profiles (or default) as liquibase contexts
        String[] profiles = ArrayUtils.isNotEmpty(env.getActiveProfiles()) ? env.getActiveProfiles() : env.getDefaultProfiles();
        springLiquibase.setContexts(StringUtils.join(profiles, ","));
        return springLiquibase;
    }

    /**
     * The data source
     *
     * @return the datasource
     * @throws NamingException if there isn't a jndi bean
     */
    @Bean(name = "dataSource")
    @Profile({SPRING_PROFILE_JNDI_PERSISTENCE, SPRING_PROFILE_PRODUCTION})
    public DataSource getDataSource() throws NamingException {
        JndiTemplate jndi = new JndiTemplate();
        return new TransactionAwareDataSourceProxy(
                new LazyConnectionDataSourceProxy(
                        jndi.lookup("java:comp/env/jdbc/EmmiManagerDS", DataSource.class)
                ));
    }

    /**
     * The data source
     *
     * @return the datasource
     * @throws NamingException if there isn't a jndi bean
     */
    @Bean
    @Profile({SPRING_PROFILE_JNDI_PERSISTENCE, SPRING_PROFILE_PRODUCTION})
    public DataSource getDdlDataSource() throws NamingException {
        JndiTemplate jndi = new JndiTemplate();
        return jndi.lookup("java:comp/env/jdbc/EmmiManagerDdlDS", DataSource.class);
    }

    /**
     * Datasource for testing
     *
     * @return data source
     */
    @Bean(name = "dataSource")
    @Profile({SPRING_PROFILE_TEST, SPRING_PROFILE_H2})
    public DataSource getTestingDataSource(Environment env) {
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName("org.h2.Driver");
        String dbName = env.acceptsProfiles(SPRING_PROFILE_TEST, SPRING_PROFILE_H2_IN_MEMORY) ?
                "mem:EmmiManager" : "./database/target/h2_runtime/EmmiManager";
        ds.setUrl("jdbc:h2:" + dbName + ";DB_CLOSE_DELAY=-1");
        ds.setUsername("sa");
        ds.setPassword("");
        if (env.acceptsProfiles(SPRING_PROFILE_H2)) {
            try {
                Server server = Server.createTcpServer().start();
                LOGGER.info("Database server started and connection is open.");
                LOGGER.info("Connection URL: jdbc:h2:{}/{}", server.getURL(), dbName);
            } catch (SQLException e) {
                LOGGER.error("Problem connecting to in memory database", e);
            }
        }
        return new TransactionAwareDataSourceProxy(new LazyConnectionDataSourceProxy(ds));
    }

    @Bean(name = "transactionManager")
    @Profile({SPRING_PROFILE_TEST})
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(emf);
        return transactionManager;
    }

    /**
     * auto logging bean
     *
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
        properties.setProperty("jadira.usertype.javaZone", "jvm");
        properties.setProperty("jadira.usertype.databaseZone", "UTC");
        properties.setProperty("javax.persistence.validation.mode", "ddl, callback");
        properties.setProperty("org.hibernate.envers.audit_table_suffix", "_audit");
        properties.setProperty("org.hibernate.envers.default_schema", "audit");
        properties.setProperty("org.hibernate.envers.revision_field_name", "revision");
        properties.setProperty("org.hibernate.envers.revision_type_field_name", "revision_type");
        properties.setProperty("hibernate.cache.use_second_level_cache", "true");
        properties.setProperty("hibernate.cache.use_query_cache", "true");
        properties.setProperty("hibernate.generate_statistics", "true");
        properties.setProperty("hibernate.cache.region.factory_class", "com.emmisolutions.emmimanager.persistence.configuration.HazelcastCacheRegionFactory");
        properties.setProperty("hibernate.cache.use_minimal_puts", "true");
        properties.setProperty("hibernate.jdbc.batch_size", batchSize);
        properties.setProperty("hibernate.cache.hazelcast.use_lite_member", "true");
        return properties;
    }


}
