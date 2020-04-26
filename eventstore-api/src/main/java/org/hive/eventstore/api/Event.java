package org.hive.eventstore.api;

import static org.hive.eventstore.api.EventFactory.prepareEvent;

public interface Event<T> {

    static <T> Event<T> withPayload(T payload) {
        return prepareEvent(payload, EventMetadata.empty());
    }

    static <T> Event<T> createEvent(T payload, EventMetadata metadata) {
        return prepareEvent(payload, metadata);
    }

    T payload();

    EventMetadata metadata();

}
