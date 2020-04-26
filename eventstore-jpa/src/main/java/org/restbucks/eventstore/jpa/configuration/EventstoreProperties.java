package org.restbucks.eventstore.jpa.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("restbucks.eventstore.jpa")
public class EventstoreProperties {
    private String[] basePackages;
    private boolean enabled;
}
