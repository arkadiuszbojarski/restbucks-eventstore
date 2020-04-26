package org.restbucks.test;

import org.hive.eventstore.configuration.TypeScan;
import org.hive.eventstore.mapping.annotation.AnnotationClassMapper;
import org.restbucks.test.events.DomainEvent;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.hive.eventstore.mapping.annotation.AnnotationClassMapper.annotationTypeMapper;

@Configuration
@TypeScan({"org.restbucks.test.types", "org.restbucks.test.events"})
@SpringBootApplication
public class TestApplication {
    public static void main(String[] args) {
        SpringApplication.run(TestApplication.class);
    }

    @Bean
    public AnnotationClassMapper<DomainEvent, String> domainEventAnnotationClassMapper() {
        return annotationTypeMapper(DomainEvent.class, DomainEvent::type);
    }
}
