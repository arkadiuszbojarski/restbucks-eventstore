package org.restbucks.eventstore.event;

import org.restbucks.eventstore.metadata.EventMetadata;

public interface Event<T> {
    static <T> Event<T> withPayload(T payload) {
        return EventFactory.withPayload(payload);
    }

    static <T> Event<T> createEvent(T payload, EventMetadata metadata) {
        return EventFactory.createEvent(payload, metadata);
    }

    T payload();
    EventMetadata metadata();

}
