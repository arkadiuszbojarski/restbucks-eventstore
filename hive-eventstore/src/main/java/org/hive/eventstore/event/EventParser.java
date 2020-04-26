package org.hive.eventstore.event;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.hive.eventstore.api.Event;
import org.hive.eventstore.mapping.registry.TypeMappingRegistry;
import org.hive.eventstore.metadata.AttributeParser;
import org.hive.eventstore.payload.Mapper;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.function.Function;

import static org.hive.eventstore.api.Event.createEvent;
import static org.hive.eventstore.exception.MissingClassMappingForTypeException.missingClassMappingForType;

@Component
@RequiredArgsConstructor
public class EventParser implements Function<EventDTO, Event> {
    private final TypeMappingRegistry typeRegistry;
    private final AttributeParser attributeParser;
    private final Mapper<String> mapper;

    @SneakyThrows
    public Event apply(EventDTO event) {
        final var type = event.getType();
        final var typeClass = typeRegistry
                .getClassForType(type)
                .orElseThrow(() -> missingClassMappingForType(type));

        final var payload = mapper.deserialize(event.getPayload(), typeClass);
        final var metadata = attributeParser.apply(Set.copyOf(event.getAttributes()));

        return createEvent(payload, metadata);
    }

}
