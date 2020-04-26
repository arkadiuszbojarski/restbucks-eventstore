package org.restbucks.eventstore;

import org.restbucks.eventstore.event.Event;
import org.restbucks.eventstore.metadata.EventMetadata;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EventStore {
    List<Event> findEventsOfStream(UUID streamID);
    List<Event> findEventsWithMetadata(EventMetadata metadata);
    Optional<Event> findEventByID(UUID eventID);

    void updateEventMetadata(UUID eventID, EventMetadata metadata);

    void appendEventToStream(UUID streamID, Event event);
}
