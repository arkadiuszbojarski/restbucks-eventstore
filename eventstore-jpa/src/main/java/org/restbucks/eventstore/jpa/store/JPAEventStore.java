package org.restbucks.eventstore.jpa.store;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.restbucks.eventstore.event.Event;
import org.restbucks.eventstore.metadata.EventMetadata;
import org.restbucks.eventstore.EventStore;
import org.restbucks.eventstore.jpa.event.EventParser;
import org.restbucks.eventstore.jpa.stream.EventAppender;
import org.restbucks.eventstore.jpa.event.PersistentEventRepository;
import org.restbucks.eventstore.jpa.metadata.MetadataParser;
import org.restbucks.eventstore.jpa.stream.StreamRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;

@Slf4j(topic = "org.restbucks.EventStore")
@Component
@Transactional
@RequiredArgsConstructor
public class JPAEventStore implements EventStore {
    private final PersistentEventRepository eventRepository;
    private final StreamRepository streamRepository;
    private final MetadataParser metadataParser;
    private final EventAppender eventAppender;
    private final EventParser eventParser;

    @Override
    public List<Event> findEventsOfStream(final UUID streamID) {
        requireNonNull(streamID);

        return streamRepository.findStreamByID(streamID)
                .events().stream()
                .map(p -> eventParser.apply(p))
                .collect(toList());
    }

    @SneakyThrows
    @Override
    public List<Event> findEventsWithMetadata(EventMetadata metadata) {
        requireNonNull(metadata);

//        QPersistentEvent.persistentEvent.metadata.

        throw new UnsupportedOperationException();
    }

    @Override
    @SneakyThrows
    public void appendEventToStream(UUID streamID, Event event) {
        eventAppender.appendEventToStream(streamID, event);
    }

    @Override
    public Optional<Event> findEventByID(final UUID eventID) {
        return eventRepository
                .findById(eventID)
                .map(p -> eventParser.apply(p));
    }

    @Override
    public void updateEventMetadata(UUID eventID, EventMetadata metadata) {
        requireNonNull(eventID);
        requireNonNull(metadata);

        eventRepository
                .findById(eventID)
                .ifPresent(event -> metadataParser.parseMetadata(metadata));
    }

}