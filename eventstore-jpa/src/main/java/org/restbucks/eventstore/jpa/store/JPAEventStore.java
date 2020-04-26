package org.restbucks.eventstore.jpa.store;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.restbucks.eventstore.EventStore;
import org.restbucks.eventstore.event.Event;
import org.restbucks.eventstore.jpa.event.EventRepository;
import org.restbucks.eventstore.jpa.metadata.MetadataUpdater;
import org.restbucks.eventstore.jpa.stream.EventAppender;
import org.restbucks.eventstore.metadata.EventMetadata;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j(topic = "org.restbucks.EventStore")
@Component
@Transactional
@RequiredArgsConstructor
public class JPAEventStore implements EventStore {
    private final MetadataUpdater metadataUpdater;
    private final EventRepository eventRepository;
    private final EventAppender eventAppender;

    @Override
    public List<Event> findEventsOfStream(final UUID streamID) {
        return eventRepository.findEventsOfStream(streamID);
    }

    @Override
    public List<Event> findEventsWithMetadata(EventMetadata metadata) {
        return eventRepository.findEventsWithMetadata(metadata);
    }

    @Override
    public void appendEventToStream(UUID streamID, Event event) {
        eventAppender.appendEventToStream(streamID, event);
    }

    @Override
    public Optional<Event> findEventByID(final UUID eventID) {
        return eventRepository.getEvent(eventID);
    }

    @Override
    public void updateEventMetadata(UUID eventID, EventMetadata metadata) {
        metadataUpdater.updateMetadata(eventID, metadata);
    }

}