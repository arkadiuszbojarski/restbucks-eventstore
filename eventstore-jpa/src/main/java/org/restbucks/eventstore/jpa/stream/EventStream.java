package org.restbucks.eventstore.jpa.stream;


import lombok.*;
import org.restbucks.eventstore.event.AppendedEvent;
import org.restbucks.eventstore.jpa.event.DomainEvent;
import org.springframework.data.domain.AbstractAggregateRoot;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static java.util.List.copyOf;
import static java.util.Objects.requireNonNull;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
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

    public void append(DomainEvent event) {
        requireNonNull(event);
        event.appendToStream(this);
        events.add(event);

        registerEvent(AppendedEvent.of(identifier, event.getEventID()));
    }

    public List<DomainEvent> events() {
        return copyOf(events);
    }

}
