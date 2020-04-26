package org.hive.eventstore.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.mybatis.spring.annotation.MapperScan;
import org.hive.eventstore.annotation.TypeAlias;
import org.hive.eventstore.mapping.annotation.AnnotationClassMapper;
import org.hive.eventstore.payload.JSONMapper;
import org.hive.eventstore.payload.Mapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan("org.hive.eventstore")
@EnableJpaRepositories("org.hive.eventstore.configuration")
@MapperScan("org.hive.eventstore.configuration")
@EntityScan("org.hive.eventstore")
@PropertySource("classpath:mybatis.properties")
@ConditionalOnProperty(value = "hive.eventstore.enabled", havingValue = "true", matchIfMissing = true)
public class HiveEventStoreConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public Mapper<String> mapper() {
        final var mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        return new JSONMapper(mapper);
    }

    @Bean
    @ConditionalOnMissingBean
    public AnnotationClassMapper<TypeAlias, String> typeAliasAnnotationTypeExtractor() {
        return AnnotationClassMapper.annotationTypeMapper(TypeAlias.class, TypeAlias::type);
    }
}
