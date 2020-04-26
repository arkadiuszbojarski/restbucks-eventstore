package org.restbucks.eventstore;

public interface EventPublisher {
    void publishEvent(Object event);
}
