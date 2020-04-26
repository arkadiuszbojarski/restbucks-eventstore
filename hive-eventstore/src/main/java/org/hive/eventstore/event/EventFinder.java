package org.hive.eventstore.event;

import lombok.RequiredArgsConstructor;
import org.hive.eventstore.api.Event;
import org.hive.eventstore.api.EventMetadata;
import org.hive.eventstore.configuration.MyBatisEventRepository;
import org.hive.eventstore.metadata.MetadataParser;
import org.hive.eventstore.stream.AttributeVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@Service
@Transactional
@RequiredArgsConstructor
public class EventFinder {
    private final MyBatisEventRepository myBatisEventRepository;
    private final MetadataParser metadataParser;
    private final EventParser eventParser;

    public List<Event> findEventsOfStream(UUID streamID) {
        requireNonNull(streamID);
        final var events = myBatisEventRepository
                .findEventsByStreamId(streamID).stream()
                .map(eventParser)
                .collect(toList());

        return events;
    }

    public List<Event> findEventsWithMetadata(EventMetadata metadata) {
        requireNonNull(metadata);

        var parameters = metadataParser
                .apply(metadata).stream()
                .collect(toMap(AttributeVO::getName, AttributeVO::getValue));

        return myBatisEventRepository
                .findEventsWithMetadata(parameters, parameters.size()).stream()
                .map(eventParser)
                .collect(toList());
    }

    public Optional<Event> getEvent(UUID eventID) {
        final var event = myBatisEventRepository
                .findById(eventID)
                .map(eventParser);

        return event;
    }
}