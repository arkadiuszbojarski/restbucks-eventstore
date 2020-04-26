package org.restbucks.test;

import org.restbucks.eventstore.jpa.configuration.TypeScan;
import org.restbucks.eventstore.jpa.mapping.annotation.AnnotationClassMapper;
import org.restbucks.test.events.DomainEvent;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.restbucks.eventstore.jpa.mapping.annotation.AnnotationClassMapper.annotationTypeMapper;

@Configuration
@TypeScan({"org.restbucks.test.types", "org.restbucks.test.events"})
@SpringBootApplication
public class TestApplication {
    public static void main(String[] args) {
        SpringApplication.run(TestApplication.class);
    }

    @Bean
    public AnnotationClassMapper<DomainEvent> domainEventAnnotationClassMapper() {
        return annotationTypeMapper(DomainEvent.class, DomainEvent::type);
    }
}
