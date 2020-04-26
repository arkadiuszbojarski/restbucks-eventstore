package org.restbucks.eventstore.event;

import org.restbucks.eventstore.metadata.EventMetadata;

import static java.util.Objects.requireNonNull;

public class GenericEvent<T> implements Event<T> {
    private final EventMetadata metadata;
    private final T payload;

    public static <T> GenericEvent<T> withPayload(T payload) {
        return new GenericEvent<>(payload, EventMetadata.empty());
    }

    public static <T> GenericEvent<T> createEvent(T payload, EventMetadata metadata) {
        return new GenericEvent<>(payload, metadata);
    }

    private GenericEvent(T payload, EventMetadata metadata) {
        this.metadata = requireNonNull(metadata);
        this.payload = payload;
    }

    @Override
    public T payload() {
        return payload;
    }

    @Override
    public EventMetadata metadata() {
        return metadata;
    }
}
