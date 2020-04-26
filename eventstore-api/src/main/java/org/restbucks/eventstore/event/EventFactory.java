package org.restbucks.eventstore.event;

import org.restbucks.eventstore.metadata.EventMetadata;

enum EventFactory {
    ;

    public static <T> Event<T> withPayload(T payload) {
        return new GenericEvent<>(payload, EventMetadata.builder().build());
    }

    public static <T> Event<T> createEvent(T payload, EventMetadata metadata) {
        return new GenericEvent<>(payload, metadata);
    }
}
