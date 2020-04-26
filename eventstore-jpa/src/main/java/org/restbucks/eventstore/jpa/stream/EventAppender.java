package org.restbucks.eventstore.jpa.stream;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.restbucks.eventstore.event.Event;
import org.restbucks.eventstore.jpa.event.PersistentEvent;
import org.restbucks.eventstore.jpa.mapping.registry.ClassMappingRegistry;
import org.restbucks.eventstore.jpa.metadata.Attribute;
import org.restbucks.eventstore.jpa.metadata.MetadataParser;
import org.restbucks.eventstore.jpa.payload.Mapper;
import org.restbucks.eventstore.metadata.EventMetadata;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static java.util.Objects.requireNonNull;
import static org.restbucks.eventstore.jpa.exception.MissingTypeMappingForClassException.missingTypeMappingForClass;
import static org.restbucks.eventstore.metadata.MetadataAttribute.*;

@Component
@Transactional
@RequiredArgsConstructor
public class EventAppender {
    private final ClassMappingRegistry classRegistry;
    private final StreamRepository streamRepository;
    private final MetadataParser metadataParser;
    private final Mapper<String> mapper;

    @SneakyThrows
    public void appendEventToStream(UUID streamID, Event event) {
        requireNonNull(streamID);
        requireNonNull(event);
        final var metadata = requireNonNull(event.metadata());
        final var payload = requireNonNull(event.payload());

        final var persistentEvent = preparePersistentEvent(streamID, metadata, payload);
        final var stream = streamRepository.findStreamByID(streamID);

        stream.append(persistentEvent);

        streamRepository.save(stream);
    }

    private PersistentEvent preparePersistentEvent(UUID streamID, EventMetadata metadata, Object payload) {
        final UUID eventID = metadata.get(EVENT_ID.attribute());

        final var type = extractPayloadType(payload);
        final var serialized = serializePayload(payload);
        final var attributes = prepareAttributes(streamID, metadata, type);

        final var persistentEvent = PersistentEvent.create(eventID, type, serialized);
        attributes.forEach(persistentEvent::putAttribute);

        return persistentEvent;
    }

    private String serializePayload(Object payload) {
        return mapper.serialize(payload);
    }

    private List<Attribute> prepareAttributes(UUID streamID, EventMetadata metadata, String type) {
        metadata.attribute(STREAM_ID.attribute(), streamID);
        metadata.attribute(TYPE.attribute(), type);
        final var attributes = metadataParser.parseMetadata(metadata);

        return attributes;
    }

    private String extractPayloadType(Object payload) {
        final var payloadClass = payload.getClass();
        return classRegistry
                .getTypeForClass(payloadClass)
                .orElseThrow(() -> missingTypeMappingForClass(payloadClass));
    }

}
