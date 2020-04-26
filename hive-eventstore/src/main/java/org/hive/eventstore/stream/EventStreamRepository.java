package org.hive.eventstore.stream;

import java.util.Optional;
import java.util.UUID;

public interface EventStreamRepository {
    Optional<EventStream> findById(UUID streamID);

    EventStream save(EventStream stream);
}
