package org.hive.eventstore.api;

enum EventFactory {
    ;

    static <T> Event<T> prepareEvent(T payload, EventMetadata metadata) {
        return new GenericEvent<>(payload, metadata);
    }
}
