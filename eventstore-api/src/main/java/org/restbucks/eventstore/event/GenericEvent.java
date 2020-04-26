package org.restbucks.eventstore.event;

import org.restbucks.eventstore.metadata.EventMetadata;

import static java.util.Objects.requireNonNull;

class GenericEvent<T> implements Event<T> {
    private final EventMetadata metadata;
    private final T payload;

    GenericEvent(T payload, EventMetadata metadata) {
        this.metadata = requireNonNull(metadata);
        this.payload = requireNonNull(payload);
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
