package org.hive.eventstore.event;

import lombok.RequiredArgsConstructor;
import org.hive.eventstore.api.EventMetadata;
import org.hive.eventstore.metadata.MetadataParser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static java.util.Objects.requireNonNull;

@Service
@Transactional
@RequiredArgsConstructor
public class MetadataUpdaterService {
    private final EventRepository eventRepository;
    private final MetadataParser metadataParser;

    public void updateMetadata(UUID eventID, EventMetadata metadata) {
        requireNonNull(eventID);
        requireNonNull(metadata);

        final var attributes = metadataParser.apply(metadata);

        eventRepository
                .findById(eventID)
                .ifPresent(event -> attributes.forEach(event::putAttribute));
    }
}
