package org.hive.eventstore.metadata;

import lombok.RequiredArgsConstructor;
import org.hive.eventstore.api.EventMetadata;
import org.hive.eventstore.event.AttributeDTO;
import org.hive.eventstore.mapping.registry.TypeMappingRegistry;
import org.hive.eventstore.payload.Mapper;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.function.Function;

import static org.hive.eventstore.exception.MissingClassMappingForTypeException.missingClassMappingForType;

@Component
@RequiredArgsConstructor
public class AttributeParser implements Function<Set<AttributeDTO>, EventMetadata> {
    private final TypeMappingRegistry typeRegistry;
    private final Mapper<String> mapper;

    @Override
    public EventMetadata apply(final Set<AttributeDTO> attributes) {
        final var builder = EventMetadata.builder();
        for (AttributeDTO attribute : attributes) {
            final var serialized = attribute.getValue();
            final var typeClass = extractClassForAttribute(attribute);
            final var value = mapper.deserialize(serialized, typeClass);
            final var name = attribute.getName();

            builder.attribute(name, value);
        }

        return builder.build();
    }

    private Class<?> extractClassForAttribute(AttributeDTO attribute) {
        final var type = attribute.getType();
        return typeRegistry
                .getClassForType(type)
                .orElseThrow(() -> missingClassMappingForType(type));
    }
}
