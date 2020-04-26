package org.hive.eventstore.mapping.annotation;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.hive.eventstore.configuration.HiveEventStoreProperties;
import org.hive.eventstore.mapping.TypeMapper;
import org.hive.eventstore.mapping.registry.ClassMappingRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.util.AnnotatedTypeScanner;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static java.lang.String.format;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toUnmodifiableMap;
import static org.hive.eventstore.mapping.annotation.ConflictingTypeMappingException.conflictingTypeMapping;
import static org.hive.eventstore.exception.MissingTypeMappingException.missingTypeMapping;

@Slf4j
@Component
@RequiredArgsConstructor
@EnableConfigurationProperties(HiveEventStoreProperties.class)
public class AnnotationTypeMapper implements TypeMapper<String> {
    private static final String CLASS_MAPPING_MESSAGE = "Mapping %s class to '%s' type";
    private final List<AnnotationClassMapper> mappers;
    private final ClassMappingRegistry classRegistry;
    private Map<String, Class<?>> mappings;

    @Value("${restbucks.eventstore.basePackages:[]}")
    private String[] packages;

    // TODO: handle conflicting class mappings
    @PostConstruct
    void mapEventClasses() {
        this.mappings = Stream.of(packages)
                .flatMap(this::findAnnotatedClasses)
                .collect(toUnmodifiableMap(
                        this::classTypeExtraction,
                        identity(),
                        this::nonConflictingDuplicateEnsurer));

        logClassMappings();
    }
    private Class<?> nonConflictingDuplicateEnsurer(Class<?> a, Class<?> b) {
        requireOneClassMappedToOneType(a, b);
        return a;
    }

    private void requireOneClassMappedToOneType(Class<?> a, Class<?> b) {
        if (differentClassesMappedToSameType(a, b)) {
            final var type = classTypeExtraction(a);
            throw conflictingTypeMapping(a, b, type);
        }
    }

    private boolean differentClassesMappedToSameType(Class<?> a, Class<?> b) {
        return !a.equals(b);
    }

    private void logClassMappings() {
        mappings.forEach((type, eventClass) -> log.info(format(CLASS_MAPPING_MESSAGE, eventClass, type)));
    }

    private Stream<Class<?>> findAnnotatedClasses(String basePackage) {
        return mappers.stream()
                .map(AnnotationClassMapper::annotation)
                .map(AnnotatedTypeScanner::new)
                .map(scanner -> scanner.findTypes(packages))
                .flatMap(Collection::stream);
    }

    @SneakyThrows
    @Override
    public Optional<Class<?>> getClassForType(String classType) {
        return Optional.ofNullable(mappings.get(classType));
    }

    private String classTypeExtraction(Class<?> aClass) {
        return classRegistry
                .getTypeForClass(aClass)
                .orElseThrow(() -> missingTypeMapping(aClass));
    }
}
