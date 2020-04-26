package org.restbucks.eventstore.jpa.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.restbucks.eventstore.annotation.TypeAlias;
import org.restbucks.eventstore.jpa.mapping.annotation.AnnotationClassMapper;
import org.restbucks.eventstore.jpa.payload.JSONMapper;
import org.restbucks.eventstore.jpa.payload.Mapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import static org.restbucks.eventstore.jpa.mapping.annotation.AnnotationClassMapper.annotationTypeMapper;

@Configuration
@ComponentScan("org.restbucks.eventstore.jpa")
@EnableJpaRepositories("org.restbucks.eventstore.jpa")
@EntityScan("org.restbucks.eventstore.jpa")
@ConditionalOnProperty(value = "restbucks.eventstore.enabled", havingValue = "true", matchIfMissing = true)
public class JPAEventStoreConfiguration {

    @Bean
    public Mapper mapper() {
        final var mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        return new JSONMapper(mapper);
    }

    @Bean
    public AnnotationClassMapper<TypeAlias> typeAliasAnnotationTypeExtractor() {
        return annotationTypeMapper(TypeAlias.class, TypeAlias::type);
    }
}
