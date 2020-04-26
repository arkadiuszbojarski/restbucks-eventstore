package org.restbucks.eventstore.event;

import org.restbucks.eventstore.metadata.EventMetadata;

public interface Event<T> {
    T payload();
    EventMetadata metadata();
}
