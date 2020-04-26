package org.restbucks.eventstore.jpa.metadata;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.restbucks.eventstore.jpa.mapping.registry.TypeMappingRegistry;
import org.restbucks.eventstore.metadata.EventMetadata;
import org.restbucks.eventstore.jpa.payload.Mapper;
import org.springframework.stereotype.Component;

import java.util.Set;

import static org.restbucks.eventstore.jpa.exception.MissingClassMappingForTypeException.missingClassMappingForType;

@Component
@RequiredArgsConstructor
public class AttributeParser {
    private final TypeMappingRegistry typeRegistry;
    private final Mapper<String> mapper;

    @SneakyThrows
    public EventMetadata parseAttributes(final Set<Attribute> attributes) {
        final var metadata = new EventMetadata();
        for (Attribute attribute : attributes) {
            final var serialized = attribute.getValue();
            final var typeClass = extractClassForAttribute(attribute);
            final var value = mapper.deserialize(serialized, typeClass);
            final var name = attribute.getName();

            metadata.attribute(name, value);
        }

        return metadata;
    }

    private Class<?> extractClassForAttribute(Attribute attribute) {
        final var type = attribute.getType();
        return typeRegistry
                .getClassForType(type)
                .orElseThrow(() -> missingClassMappingForType(type));
    }
}
