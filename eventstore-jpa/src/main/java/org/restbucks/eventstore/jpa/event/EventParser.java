package org.restbucks.eventstore.jpa.event;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.restbucks.eventstore.event.Event;
import org.restbucks.eventstore.event.GenericEvent;
import org.restbucks.eventstore.jpa.mapping.registry.TypeMappingRegistry;
import org.restbucks.eventstore.jpa.metadata.AttributeParser;
import org.restbucks.eventstore.jpa.payload.Mapper;
import org.springframework.stereotype.Component;

import java.util.Set;

import static org.restbucks.eventstore.jpa.exception.MissingClassMappingForTypeException.missingClassMappingForType;

@Component
@RequiredArgsConstructor
public class EventParser {
    private final TypeMappingRegistry typeRegistry;
    private final AttributeParser attributeParser;
    private final Mapper<String> mapper;

    @SneakyThrows
    public Event<?> apply(PersistentEvent event) {
        final var type = event.getType();
        final var typeClass = typeRegistry
                .getClassForType(type)
                .orElseThrow(() -> missingClassMappingForType(type));

        final var payload = mapper.deserialize(event.getPayload(), typeClass);
        final var metadata = attributeParser.parseAttributes(Set.copyOf(event.getMetadata().values()));

        return GenericEvent.createEvent(payload, metadata);
    }

}
