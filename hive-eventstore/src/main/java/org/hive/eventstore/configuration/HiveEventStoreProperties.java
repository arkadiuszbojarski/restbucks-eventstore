package org.hive.eventstore.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("hive.eventstore")
public class HiveEventStoreProperties {
    private String[] basePackages;
    private boolean enabled;
}
