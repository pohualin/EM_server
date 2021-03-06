package com.emmisolutions.emmimanager.persistence.configuration;


import com.emmisolutions.emmimanager.persistence.configuration.serializer.SpecificationsKryoStreamSerializer;
import com.hazelcast.config.*;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.instance.HazelcastInstanceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.transaction.TransactionAwareCacheManagerProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.domain.Specifications;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

import static com.emmisolutions.emmimanager.config.Constants.SPRING_PROFILE_DEVELOPMENT;
import static com.emmisolutions.emmimanager.config.Constants.SPRING_PROFILE_TEST;

@Configuration
@EnableCaching
public class CacheConfiguration extends CachingConfigurerSupport {

    private static HazelcastInstance hazelcastInstance;
    private final Logger log = LoggerFactory.getLogger(CacheConfiguration.class);
    @Resource
    private Environment env;

    @Value("${cache.cluster.group.name:development_cluster}")
    private String clusterGroupName;

    @Value("${cache.cluster.required.min.size:1}")
    private String minNumberOfClusterMembersRequiredToStartServer;

    @Value("${cache.cluster.enabled:true}")
    private boolean clusterEnabled;

    /**
     * @return the unique instance.
     */
    @Bean(name = "hazelCast")
    public static HazelcastInstance getHazelcastInstance() {
        return hazelcastInstance;
    }

    @PreDestroy
    public void destroy() {
        log.info("Closing Cache Manager");
        Hazelcast.shutdownAll();
    }

    @Bean
    @Override
    public CacheManager cacheManager() {
        return new TransactionAwareCacheManagerProxy(
                new com.hazelcast.spring.cache.HazelcastCacheManager(hazelcastInstance));
    }

    @PostConstruct
    private HazelcastInstance hazelcastInstance() {
        final Config config = new Config();
        config.setInstanceName("emmiManager");

        // allow multiple groups in the same multi-cast area
        config.getGroupConfig().setName(clusterGroupName);

        // add custom serializers here
        config.getSerializationConfig()
                .addSerializerConfig(
                        new SerializerConfig()
                                .setTypeClass(Specifications.class)
                                .setImplementation(new SpecificationsKryoStreamSerializer(true, 2))
                );

        config.getNetworkConfig().setPort(5701);
        config.getNetworkConfig().setPortAutoIncrement(true);
        config.setProperty("hazelcast.version.check.enabled", "false");

        if (env.acceptsProfiles(SPRING_PROFILE_DEVELOPMENT, SPRING_PROFILE_TEST)) {
            config.setProperty("hazelcast.local.localAddress", "127.0.0.1");
            config.getNetworkConfig().getJoin().getAwsConfig().setEnabled(false);
            config.getNetworkConfig().getJoin().getMulticastConfig().setEnabled(false);
            config.getNetworkConfig().getJoin().getTcpIpConfig().setEnabled(false);
        } else {
            config.getNetworkConfig().getJoin().getMulticastConfig().setEnabled(clusterEnabled);
            config.setProperty("hazelcast.initial.min.cluster.size", minNumberOfClusterMembersRequiredToStartServer);
        }

        config.getMapConfigs().put("default", initializeDefaultMapConfig());
        config.getMapConfigs().put("com.emmisolutions.emmimanager.model.*", initializeDomainMapConfig());

        hazelcastInstance = HazelcastInstanceFactory.newHazelcastInstance(config);
        return hazelcastInstance;
    }

    private MapConfig initializeDefaultMapConfig() {
        MapConfig mapConfig = new MapConfig();

        /*
            Number of backups. If 1 is set as the backup-count for example,
            then all entries of the map will be copied to another JVM for
            fail-safety. Valid numbers are 0 (no backup), 1, 2, 3.
         */
        mapConfig.setBackupCount(0);

        /*
            Valid values are:
            NONE (no eviction),
            LRU (Least Recently Used),
            LFU (Least Frequently Used).
            NONE is the default.
         */
        mapConfig.setEvictionPolicy(EvictionPolicy.LRU);

        /*
            Maximum size of the map. When max size is reached,
            map is evicted based on the policy defined.
            Any integer between 0 and Integer.MAX_VALUE. 0 means
            Integer.MAX_VALUE. Default is 0.
         */
        mapConfig.setMaxSizeConfig(new MaxSizeConfig(0, MaxSizeConfig.MaxSizePolicy.USED_HEAP_SIZE));

        /*
            When max. size is reached, specified percentage of
            the map will be evicted. Any integer between 0 and 100.
            If 25 is set for example, 25% of the entries will
            get evicted.
         */
        mapConfig.setEvictionPercentage(25);

        return mapConfig;
    }

    private MapConfig initializeDomainMapConfig() {
        MapConfig mapConfig = new MapConfig();

        mapConfig.setTimeToLiveSeconds(env.getProperty("cache.timeToLiveSeconds", Integer.class, 3600));
        return mapConfig;
    }
}
