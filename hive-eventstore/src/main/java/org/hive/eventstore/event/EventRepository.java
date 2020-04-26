package org.hive.eventstore.event;

import java.util.Optional;
import java.util.UUID;

public interface EventRepository {
    Optional<DomainEvent> findById(UUID eventID);
}
