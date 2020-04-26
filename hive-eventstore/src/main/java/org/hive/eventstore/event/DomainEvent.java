package org.hive.eventstore.event;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hive.eventstore.metadata.EventAttribute;
import org.hive.eventstore.stream.AttributeVO;
import org.hive.eventstore.stream.EventStream;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static java.util.Objects.requireNonNull;
import static org.hive.eventstore.api.MetadataAttribute.*;
import static org.hive.eventstore.event.EventAlreadyAppendedToDifferentStreamException.eventAlreadyAppendedToDifferentStream;

@Getter
@Entity
@Table(name = "DOMAINEVENT")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class DomainEvent {

    private static final Set<String> UNMODIFIABLE_ATTRIBUTES = Set.of(
            EVENT_ID.attribute(),
            STREAM_ID.attribute(),
            TIMESTAMP.attribute());

    @Id
    @EqualsAndHashCode.Include
    private UUID eventID;
    private String type;
    private String payload;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "event", orphanRemoval = true)
    @MapKey(name = "name")
    private Map<String, EventAttribute> metadata = new HashMap<>();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "streamID")
    private EventStream stream;

    public static DomainEvent create(UUID eventID, String type, String payload) {
        final var event = new DomainEvent();
        event.eventID = requireNonNull(eventID);
        event.type = requireNonNull(type);
        event.payload = requireNonNull(payload);

        return event;
    }

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

    public void putAttribute(AttributeVO attribute) {
        requireNonNull(attribute);
        final var name = attribute.getName();
        final var value = attribute.getValue();
        final var type = attribute.getType();
        final var eventAttribute = EventAttribute.of(name, value, type);

        if (isPuttingAttributeAllowed(eventAttribute)) {
            metadata.put(name, eventAttribute);
            eventAttribute.addToEvent(this);
        }
    }

    private boolean isPuttingAttributeAllowed(EventAttribute attribute) {
        return !isAttributePresent(attribute) || isAttributeModifiable(attribute);
    }

    private boolean isAttributePresent(EventAttribute attribute) {
        return this.metadata.containsKey(attribute.getName());
    }

    private static boolean isAttributeModifiable(EventAttribute attribute) {
        return !UNMODIFIABLE_ATTRIBUTES.contains(attribute.getName());
    }
}
