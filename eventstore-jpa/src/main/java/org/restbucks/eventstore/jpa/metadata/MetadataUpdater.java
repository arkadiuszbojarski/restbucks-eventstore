package org.restbucks.eventstore.jpa.metadata;

import lombok.RequiredArgsConstructor;
import org.restbucks.eventstore.jpa.event.JPAEventRepository;
import org.restbucks.eventstore.metadata.EventMetadata;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toUnmodifiableList;

@Component
@Transactional
@RequiredArgsConstructor
public class MetadataUpdater {
    private final JPAEventRepository eventRepository;
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
