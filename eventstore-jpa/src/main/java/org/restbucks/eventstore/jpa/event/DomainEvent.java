package org.restbucks.eventstore.jpa.event;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.restbucks.eventstore.jpa.metadata.Attribute;
import org.restbucks.eventstore.jpa.stream.EventStream;

import javax.persistence.*;
import java.util.*;

import static java.util.Objects.requireNonNull;
import static org.restbucks.eventstore.jpa.exception.EventAlreadyAppendedToDifferentStreamException.eventAlreadyAppendedToDifferentStream;
import static org.restbucks.eventstore.metadata.MetadataAttribute.UNMODIFIABLE_ATTRIBUTES;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class DomainEvent {

    @Id
    @EqualsAndHashCode.Include
    private UUID eventID;
    private String type;
    private String payload;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "event", orphanRemoval = true)
    @MapKey(name = "name")
    private Map<String, Attribute> metadata = new HashMap<>();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "streamID")
    private EventStream stream;

    /* CREATION */

    public static DomainEvent create(UUID eventID, String type, String payload) {
        final var event = new DomainEvent();
        event.eventID = requireNonNull(eventID);
        event.type = requireNonNull(type);
        event.payload = requireNonNull(payload);

        return event;
    }

    /* APPENDING TO STREAM */

    public void appendToStream(EventStream stream) {
        requireNonNull(stream);
        requireNotAppendedToDifferentStream(stream);

        this.stream = stream;
    }

    private void requireNotAppendedToDifferentStream(EventStream stream) {
        if (isAppendedToDifferentStream(stream)) {
            throw eventAlreadyAppendedToDifferentStream(eventID, this.stream.getIdentifier(), stream.getIdentifier());
        }
    }
    private boolean isAppendedToDifferentStream(EventStream stream) {
        return isAppendedToAnyStream() && !isAppendedToConcreteStream(stream);
    }
    private boolean isAppendedToAnyStream() {
        return this.stream != null;
    }
    private boolean isAppendedToConcreteStream(EventStream stream) {
        return stream.equals(this.stream);
    }

    /* PUTTING ATTRIBUTE */

    public void putAttribute(Attribute attribute) {
        requireNonNull(attribute);
        final var name = requireNonNull(attribute.getName());

        if (isPuttingAttributeAllowed(attribute)) {
            metadata.put(name, attribute);
            attribute.addToEvent(this);
        }
    }

    private boolean isPuttingAttributeAllowed(Attribute attribute) {
        return !isAttributePresent(attribute) || isAttributeModifiable(attribute);
    }
    private boolean isAttributePresent(Attribute attribute) {
        return this.metadata.containsKey(attribute.getName());
    }
    private static boolean isAttributeModifiable(Attribute attribute) {
        return !UNMODIFIABLE_ATTRIBUTES.contains(attribute.getName());
    }
}
