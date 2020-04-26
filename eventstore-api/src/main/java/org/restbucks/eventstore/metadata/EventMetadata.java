package org.restbucks.eventstore.metadata;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static java.util.Objects.requireNonNull;
import static java.util.UUID.randomUUID;
import static org.restbucks.eventstore.metadata.MetadataAttribute.*;

public class EventMetadata {
    private final Map<String, Object> attributes = new HashMap<>();

    public static EventMetadata of(final Map<String, Object> attributes) {
        requireNonNull(attributes);
        final var eventMetadata = new EventMetadata();
        for (String name : attributes.keySet()) {
            final var value = attributes.get(name);
            eventMetadata.attribute(name, value);
        }

        return eventMetadata;
    }

    public static EventMetadata empty() {
        final var metadata = new EventMetadata();
        metadata.eventID(randomUUID());
        metadata.timestamp(Instant.now());

        return metadata;
    }

    public EventMetadata eventID(final UUID eventID) {
        requireNonNull(eventID);
        attributes.put(EVENT_ID.attribute(), eventID);

        return this;
    }

    public EventMetadata timestamp(final Instant timestamp) {
        requireNonNull(timestamp);
        attributes.put(TIMESTAMP.attribute(), timestamp);

        return this;
    }

    public EventMetadata type(final String type) {
        requireNonNull(type);
        attributes.put(TYPE.attribute(), type);

        return this;
    }

    public EventMetadata attribute(final String name, final Object value) {
        requireNonNull(name);
        attributes.put(name, value);

        return this;
    }

    public Set<String> attributes() {
        return Set.copyOf(attributes.keySet());
    }

    public <T> T get(final String name) {
        return (T) attributes.get(name);
    }
}
