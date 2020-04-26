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
import static org.restbucks.eventstore.metadata.MetadataAttribute.MODIFIABLE_ATTRIBUTES;
import static org.restbucks.eventstore.jpa.exception.EventAlreadyAppendedToDifferentStreamException.eventAlreadyAppendedToDifferentStream;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PersistentEvent {

    @Id
    @EqualsAndHashCode.Include
    private UUID eventID;
    private String type;
    private String payload;

    @ElementCollection
    @CollectionTable
    @MapKeyColumn(name = "attribute_name")
    private Map<String, Attribute> metadata = new HashMap<>();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "streamID")
    private EventStream stream;

    /* CREATION */

    public static PersistentEvent create(UUID eventID, String type, String payload) {
        final var event = new PersistentEvent();
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

        if (isPuttingAttributeAllowed(name)) {
            metadata.put(name, attribute);
        }
    }

    private boolean isPuttingAttributeAllowed(String name) {
        return !isAttributePresent(name) || isAttributeModifiable(name);
    }
    private boolean isAttributePresent(String name) {
        return this.metadata.containsKey(name);
    }
    private static boolean isAttributeModifiable(String name) {
        return MODIFIABLE_ATTRIBUTES.contains(name);
    }
}
