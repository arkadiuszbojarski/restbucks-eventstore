package org.restbucks.eventstore.jpa.metadata;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.restbucks.eventstore.jpa.mapping.registry.ClassMappingRegistry;
import org.restbucks.eventstore.jpa.payload.Mapper;
import org.restbucks.eventstore.metadata.EventMetadata;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toUnmodifiableList;
import static org.restbucks.eventstore.jpa.exception.MissingTypeMappingForClassException.missingTypeMappingForClass;

@Component
@RequiredArgsConstructor
public class MetadataParser {
    private final ClassMappingRegistry classRegistry;
    private final Mapper<String> mapper;

    @SneakyThrows
    public List<Attribute> parseMetadata(EventMetadata metadata) {
        return metadata.attributes().stream()
                .map(name -> Optional
                        .ofNullable(metadata.get(name))
                        .map(value -> {
                            final String classType = extractType(value);

                            final var serialized = mapper.serialize(value);
                            return Attribute.of(name, serialized, classType);
                        }).orElse(Attribute.empty(name))).collect(toUnmodifiableList());
    }

    private String extractType(Object value) {
        final var valueClass = value.getClass();
        return classRegistry
                .getTypeForClass(valueClass)
                .orElseThrow(() -> missingTypeMappingForClass(valueClass));
    }
}
