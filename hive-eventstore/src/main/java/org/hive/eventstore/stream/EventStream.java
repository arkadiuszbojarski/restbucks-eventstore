package org.hive.eventstore.stream;


import lombok.*;
import org.hive.eventstore.event.DomainEvent;
import org.springframework.data.domain.AbstractAggregateRoot;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static java.util.Objects.requireNonNull;

@Entity
@Table(name = "EVENTSTREAM")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class EventStream extends AbstractAggregateRoot<EventStream> {

    @Id
    @Getter
    @EqualsAndHashCode.Include
    private UUID identifier;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "stream", orphanRemoval = true)
    private Set<DomainEvent> events = new HashSet<>();

    public static EventStream of(final UUID identifier) {
        final var eventStream = new EventStream();
        eventStream.identifier = requireNonNull(identifier);

        return eventStream;
    }

    public void append(DomainEventVO event) {
        requireNonNull(event);

        final var eventID = event.getEventID();
        final var type = event.getType();
        final var payload = event.getPayload();

        final var domainEvent = DomainEvent.create(eventID, type, payload);
        event.getAttributes().forEach(domainEvent::putAttribute);

        domainEvent.appendToStream(this);
        events.add(domainEvent);
    }

}
