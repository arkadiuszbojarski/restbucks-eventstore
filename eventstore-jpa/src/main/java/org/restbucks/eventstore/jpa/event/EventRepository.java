package org.restbucks.eventstore.jpa.event;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import lombok.RequiredArgsConstructor;
import org.restbucks.eventstore.event.Event;
import org.restbucks.eventstore.jpa.metadata.Attribute;
import org.restbucks.eventstore.jpa.metadata.MetadataParser;
import org.restbucks.eventstore.jpa.metadata.QAttribute;
import org.restbucks.eventstore.jpa.stream.StreamRepository;
import org.restbucks.eventstore.metadata.EventMetadata;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.StreamSupport;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;

@Repository
@Transactional
@RequiredArgsConstructor
public class EventRepository {
    private final JPAEventRepository eventRepository;
    private final StreamRepository streamRepository;
    private final MetadataParser metadataParser;
    private final EventParser eventParser;

    public List<Event> findEventsOfStream(UUID streamID) {
        requireNonNull(streamID);
        final var events = streamRepository
                .findStreamByID(streamID)
                .events().stream()
                .map(eventParser)
                .collect(toList());

        return events;
    }

    public List<Event> findEventsWithMetadata(EventMetadata metadata) {
        requireNonNull(metadata);
        final var predicate = prepareEventPredicate(metadata);
        final var events = StreamSupport
                .stream(eventRepository.findAll(predicate).spliterator(), false)
                .map(eventParser)
                .collect(toList());

        return events;
    }

    private BooleanExpression prepareEventPredicate(EventMetadata metadata) {
        final var attributes = attributesPredicate(metadata);
        final var persistentEvent = QDomainEvent.domainEvent;
        final var predicate = persistentEvent
                .metadata
                .containsValue(attributes);

        return predicate;
    }

    private JPQLQuery<Attribute> attributesPredicate(EventMetadata metadata) {
        final var predicate = QAttribute.attribute;
        final var expression = JPAExpressions.selectFrom(predicate);

        metadataParser
                .apply(metadata)
                .forEach(attribute -> expression
                        .where(predicate.name.eq(attribute.getName()))
                        .where(predicate.value.eq(attribute.getValue()))
                        .where(predicate.type.eq(attribute.getType())));

        return expression;
    }

    public Optional<Event> getEvent(UUID eventID) {
        final var event = eventRepository
                .findById(eventID)
                .map(eventParser);

        return event;
    }
}