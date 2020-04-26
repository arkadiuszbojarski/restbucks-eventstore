package org.restbucks.eventstore.jpa.metadata;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.restbucks.eventstore.event.Event;
import org.restbucks.eventstore.jpa.mapping.registry.TypeMappingRegistry;
import org.restbucks.eventstore.metadata.EventMetadata;
import org.restbucks.eventstore.jpa.payload.Mapper;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.function.Function;

import static org.restbucks.eventstore.jpa.exception.MissingClassMappingForTypeException.missingClassMappingForType;

@Component
@RequiredArgsConstructor
public class AttributeParser implements Function<Set<Attribute>, EventMetadata> {
    private final TypeMappingRegistry typeRegistry;
    private final Mapper<String> mapper;

    @Override
    public EventMetadata apply(final Set<Attribute> attributes) {
        final var builder = EventMetadata.builder();
        for (Attribute attribute : attributes) {
            final var serialized = attribute.getValue();
            final var typeClass = extractClassForAttribute(attribute);
            final var value = mapper.deserialize(serialized, typeClass);
            final var name = attribute.getName();

            builder.attribute(name, value);
        }

        return builder.build();
    }

    private Class<?> extractClassForAttribute(Attribute attribute) {
        final var type = attribute.getType();
        return typeRegistry
                .getClassForType(type)
                .orElseThrow(() -> missingClassMappingForType(type));
    }
}
