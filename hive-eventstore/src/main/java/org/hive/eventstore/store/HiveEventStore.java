package org.hive.eventstore.store;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hive.eventstore.api.EventStore;
import org.hive.eventstore.api.Event;
import org.hive.eventstore.event.EventFinder;
import org.hive.eventstore.event.MetadataUpdaterService;
import org.hive.eventstore.stream.EventAppenderService;
import org.hive.eventstore.api.EventMetadata;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
@Transactional
@RequiredArgsConstructor
public class HiveEventStore implements EventStore {
    private final MetadataUpdaterService metadataUpdater;
    private final EventAppenderService eventAppender;
    private final EventFinder eventFinder;

    @Override
    public List<Event> findEventsOfStream(final UUID streamID) {
        return eventFinder.findEventsOfStream(streamID);
    }

    @Override
    public Optional<Event> findEventByID(final UUID eventID) {
        return eventFinder.getEvent(eventID);
    }

    @Override
    public List<Event> findEventsWithMetadata(EventMetadata metadata) {
        return eventFinder.findEventsWithMetadata(metadata);
    }

    @Override
    public void appendEventsToStream(UUID streamID, Event... events) {
        eventAppender.appendEventsToStream(streamID, events);
    }

    @Override
    public void updateEventMetadata(UUID eventID, EventMetadata metadata) {
        metadataUpdater.updateMetadata(eventID, metadata);
    }

}