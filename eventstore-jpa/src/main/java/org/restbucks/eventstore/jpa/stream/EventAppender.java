package org.restbucks.eventstore.jpa.stream;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.restbucks.eventstore.jpa.event.DomainEvent;
import org.restbucks.eventstore.jpa.mapping.registry.ClassMappingRegistry;
import org.restbucks.eventstore.jpa.metadata.MetadataParser;
import org.restbucks.eventstore.jpa.payload.Mapper;
import org.restbucks.eventstore.metadata.EventMetadata;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static java.util.Objects.requireNonNull;
import static java.util.UUID.randomUUID;
import static org.restbucks.eventstore.jpa.exception.MissingTypeMappingForClassException.missingTypeMappingForClass;
import static org.restbucks.eventstore.metadata.MetadataAttribute.STREAM_ID;
import static org.restbucks.eventstore.metadata.MetadataAttribute.TYPE;

@Component
@Transactional
@RequiredArgsConstructor
public class EventAppender {
    private final ClassMappingRegistry classRegistry;
    private final StreamRepository streamRepository;
    private final MetadataParser metadataParser;
    private final Mapper<String> mapper;

    @SneakyThrows
    public void appendEventToStream(UUID streamID, org.restbucks.eventstore.event.Event event) {
        requireNonNull(streamID);
        requireNonNull(event);
        final var metadata = requireNonNull(event.metadata());
        final var payload = requireNonNull(event.payload());

        final var persistentEvent = preparePersistentEvent(streamID, metadata, payload);
        final var stream = streamRepository.findStreamByID(streamID);

        stream.append(persistentEvent);

        streamRepository.save(stream);
    }

    private DomainEvent preparePersistentEvent(UUID streamID, EventMetadata basicMetadata, Object payload) {
        final var metadata = supplementMetadata(streamID, payload, basicMetadata);

        final var eventID = metadata.getEventID();
        final var type = metadata.getType(String.class);
        final var serialized = serializePayload(payload);

        final var persistentEvent = DomainEvent.create(eventID, type, serialized);
        metadataParser
                .apply(metadata).stream()
                .forEach(persistentEvent::putAttribute);

        return persistentEvent;
    }

    private EventMetadata supplementMetadata(UUID streamID, Object payload, EventMetadata metadata) {
        final var type = extractPayloadType(payload);

        final var eventID = Optional
                .ofNullable(metadata.getEventID())
                .orElse(randomUUID());

        final var timestamp = Optional
                .ofNullable(metadata.getTimestamp())
                .orElse(Instant.now());

        final var builder = EventMetadata.builder()
                .attribute(STREAM_ID.attribute(), streamID)
                .attribute(TYPE.attribute(), type)
                .timestamp(timestamp)
                .eventID(eventID);

        metadata.entrySet()
                .forEach(entry -> builder.attribute(entry.getKey(), entry.getValue()));


        return builder.build();
    }

    private String serializePayload(Object payload) {
        return mapper.serialize(payload);
    }

    private String extractPayloadType(Object payload) {
        final var payloadClass = payload.getClass();
        final var type = classRegistry
                .getTypeForClass(payloadClass)
                .orElseThrow(() -> missingTypeMappingForClass(payloadClass));

        return type;
    }

}
