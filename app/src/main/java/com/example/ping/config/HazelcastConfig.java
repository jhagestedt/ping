package com.example.ping.config;

import com.hazelcast.config.Config;
import com.hazelcast.config.DiscoveryStrategyConfig;
import com.hazelcast.config.GroupConfig;
import com.hazelcast.config.ManagementCenterConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.eureka.one.EurekaOneDiscoveryStrategyFactory;
import com.hazelcast.eureka.one.EurekaOneProperties;
import com.netflix.appinfo.ApplicationInfoManager;
import com.netflix.discovery.EurekaClient;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "cluster")
public class HazelcastConfig {

    private String group;

    private String instance;

    private Integer port = 5701;

    private ManagementCenterConfig management = new ManagementCenterConfig();

    @Autowired(required = false)
    private ApplicationInfoManager applicationInfoManager;

    @Autowired(required = false)
    private EurekaClient eurekaClient;

    @Bean
    public HazelcastInstance cluster() {
        log.info("cluster() initializing instance: {}", instance);
        Config config = new Config(instance);
        config.setManagementCenterConfig(management);

        config.getNetworkConfig()
            .setPort(port)
            .setPortAutoIncrement(true);

        config.getNetworkConfig().getJoin().getTcpIpConfig().setEnabled(false);
        config.getNetworkConfig().getJoin().getAwsConfig().setEnabled(false);
        config.getNetworkConfig().getJoin().getMulticastConfig().setEnabled(false);

        log.info("cluster() initializing group: {}", group);
        config.setGroupConfig(new GroupConfig(group));

        config.setProperty("hazelcast.discovery.enabled", "true");

        applicationInfoManager.getEurekaInstanceConfig();
        EurekaOneDiscoveryStrategyFactory.setEurekaClient(eurekaClient);

        DiscoveryStrategyConfig discovery = new DiscoveryStrategyConfig(new EurekaOneDiscoveryStrategyFactory());
        discovery.addProperty(EurekaOneProperties.SELF_REGISTRATION.key(), Boolean.TRUE);
        discovery.addProperty(EurekaOneProperties.NAMESPACE.key(), group);

        config.getNetworkConfig().getJoin().getDiscoveryConfig().addDiscoveryStrategyConfig(discovery);

        return Hazelcast.newHazelcastInstance(config);
    }

}
