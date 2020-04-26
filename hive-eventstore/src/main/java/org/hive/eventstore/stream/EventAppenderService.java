package org.hive.eventstore.stream;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.hive.eventstore.api.Event;
import org.hive.eventstore.mapping.registry.ClassMappingRegistry;
import org.hive.eventstore.metadata.MetadataParser;
import org.hive.eventstore.payload.Mapper;
import org.hive.eventstore.api.EventMetadata;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static java.util.Arrays.stream;
import static java.util.Objects.requireNonNull;
import static java.util.UUID.randomUUID;
import static org.hive.eventstore.exception.MissingTypeMappingException.missingTypeMapping;
import static org.hive.eventstore.api.MetadataAttribute.STREAM_ID;
import static org.hive.eventstore.api.MetadataAttribute.TYPE;

@Service
@Transactional
@RequiredArgsConstructor
public class EventAppenderService {
    private final EventStreamRepository streamRepository;
    private final ClassMappingRegistry classRegistry;
    private final MetadataParser metadataParser;
    private final Mapper<String> mapper;

    @SneakyThrows
    public void appendEventsToStream(UUID streamID, Event... events) {
        requireNonNull(streamID);
        requireNonNull(events);

        final var stream = streamRepository
                .findById(streamID)
                .orElseGet(() -> EventStream.of(streamID));

        stream(events).forEach(event -> appendEventToStream(stream, event));

        streamRepository.save(stream);
    }

    private void appendEventToStream(EventStream stream, Event event) {
        final var streamID = requireNonNull(stream).getIdentifier();
        final var metadata = requireNonNull(event.metadata());
        final var payload = requireNonNull(event.payload());

        final var domainEvent = prepareDomainEvent(streamID, metadata, payload);

        stream.append(domainEvent);
    }

    private DomainEventVO prepareDomainEvent(UUID streamID, EventMetadata basicMetadata, Object payload) {
        final var metadata = supplementMetadata(streamID, payload, basicMetadata);

        final var eventID = metadata.getEventID();
        final var type = metadata.getType();
        final var serialized = serializePayload(payload);
        final var attributes = metadataParser.apply(metadata);

        final var event = DomainEventVO.of(eventID, serialized, type, attributes);

        return event;
    }

    private EventMetadata supplementMetadata(UUID streamID, Object payload, EventMetadata metadata) {
        final var type = extractPayloadType(payload);
        final var eventID = extractEventID(metadata);
        final var timestamp = extractTimestamp(metadata);

        final var builder = EventMetadata.builder()
                .attribute(STREAM_ID.attribute(), streamID)
                .attribute(TYPE.attribute(), type)
                .timestamp(timestamp)
                .eventID(eventID);

        metadata.entrySet().forEach(entry -> builder.attribute(entry.getKey(), entry.getValue()));

        return builder.build();
    }

    private Instant extractTimestamp(EventMetadata metadata) {
        return Optional
                .ofNullable(metadata)
                .map(m -> m.getTimestamp())
                .orElse(Instant.now());
    }

    private UUID extractEventID(EventMetadata metadata) {
        return Optional
                .ofNullable(metadata)
                .map(EventMetadata::getEventID)
                .orElse(randomUUID());
    }

    private String extractPayloadType(Object payload) {
        final var payloadClass = payload != null ? payload.getClass() : Object.class;
        return classRegistry
                .getTypeForClass(payloadClass)
                .orElseThrow(() -> missingTypeMapping(payload));
    }

    private String serializePayload(Object payload) {
        return mapper.serialize(payload);
    }

}
