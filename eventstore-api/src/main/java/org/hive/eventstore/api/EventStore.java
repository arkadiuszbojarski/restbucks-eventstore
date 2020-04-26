package org.hive.eventstore.api;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EventStore {
    List<Event> findEventsOfStream(UUID streamID);

    List<Event> findEventsWithMetadata(EventMetadata metadata);

    Optional<Event> findEventByID(UUID eventID);

    void updateEventMetadata(UUID eventID, EventMetadata metadata);

    void appendEventsToStream(UUID streamID, Event... events);
}
