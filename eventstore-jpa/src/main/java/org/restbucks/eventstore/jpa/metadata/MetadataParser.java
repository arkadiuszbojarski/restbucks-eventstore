package org.restbucks.eventstore.jpa.metadata;

import lombok.RequiredArgsConstructor;
import org.restbucks.eventstore.jpa.mapping.registry.ClassMappingRegistry;
import org.restbucks.eventstore.jpa.payload.Mapper;
import org.restbucks.eventstore.metadata.EventMetadata;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static java.util.stream.Collectors.toUnmodifiableList;
import static org.restbucks.eventstore.jpa.exception.MissingTypeMappingForClassException.missingTypeMappingForClass;

@Component
@RequiredArgsConstructor
public class MetadataParser implements Function<EventMetadata, List<Attribute>> {
    private final ClassMappingRegistry classRegistry;
    private final Mapper<String> mapper;

    @Override
    public List<Attribute> apply(EventMetadata metadata) {
        return metadata.keySet().stream()
                .map(name -> Optional
                        .ofNullable(metadata.get(name))
                        .map(createAttribute(name))
                        .orElse(Attribute.empty(name)))
                .collect(toUnmodifiableList());
    }

    private Function<Object, Attribute> createAttribute(String name) {
        return value -> {
            final var classType = extractType(value);
            final var serialized = mapper.serialize(value);
            final var attribute = Attribute.of(name, serialized, classType);

            return attribute;
        };
    }

    private String extractType(Object value) {
        final var valueClass = value.getClass();
        return classRegistry
                .getTypeForClass(valueClass)
                .orElseThrow(() -> missingTypeMappingForClass(valueClass));
    }
}
