package org.restbucks.eventstore.jpa.event;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.restbucks.eventstore.event.Event;
import org.restbucks.eventstore.jpa.mapping.registry.TypeMappingRegistry;
import org.restbucks.eventstore.jpa.metadata.AttributeParser;
import org.restbucks.eventstore.jpa.payload.Mapper;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.function.Function;

import static org.restbucks.eventstore.event.Event.createEvent;
import static org.restbucks.eventstore.jpa.exception.MissingClassMappingForTypeException.missingClassMappingForType;

@Component
@RequiredArgsConstructor
public class EventParser implements Function<DomainEvent, Event> {
    private final TypeMappingRegistry typeRegistry;
    private final AttributeParser attributeParser;
    private final Mapper<String> mapper;

    @SneakyThrows
    public Event apply(DomainEvent event) {
        final var type = event.getType();
        final var typeClass = typeRegistry
                .getClassForType(type)
                .orElseThrow(() -> missingClassMappingForType(type));

        final var payload = mapper.deserialize(event.getPayload(), typeClass);
        final var metadata = attributeParser.apply(Set.copyOf(event.getMetadata().values()));

        return createEvent(payload, metadata);
    }

}
