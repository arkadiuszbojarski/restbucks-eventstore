package org.restbucks.eventstore.jpa.metadata;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.restbucks.eventstore.jpa.event.DomainEvent;

import javax.persistence.*;
import java.io.Serializable;

import static java.util.Objects.requireNonNull;
import static org.restbucks.eventstore.jpa.exception.AttributeAlreadyBelongsToDifferentEventException.attributeAlreadyBelongsToDifferentEvent;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
public class Attribute implements Serializable {

    @Id
    @GeneratedValue
    @EqualsAndHashCode.Exclude
    private Integer attributeID;
    private String name;
    private String value;
    private String type;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "eventID")
    private DomainEvent event;

    public static Attribute of(String name, String value, String type) {
        final var attribute = new Attribute();
        attribute.name = requireNonNull(name);
        attribute.value = value;
        attribute.type = type;

        return attribute;
    }

    public static Attribute empty(String name) {
        final var attribute = new Attribute();
        attribute.name = requireNonNull(name);

        return attribute;
    }

    public void addToEvent(final DomainEvent event) {
        requireNonNull(event);
        requireNotBelongingToOtherEvent(event);

        this.event = event;
    }

    private void requireNotBelongingToOtherEvent(DomainEvent event) {
        if (belongsToDifferentEvent(event)) {
            throw attributeAlreadyBelongsToDifferentEvent(attributeID, this.event.getEventID(), event.getEventID());
        }
    }
    private boolean belongsToDifferentEvent(DomainEvent event) {
        return belongsToAnyEvent() && !belongsToEvent(event);
    }
    private boolean belongsToAnyEvent() {
        return this.event != null;
    }
    private boolean belongsToEvent(DomainEvent event) {
        return event.equals(this.event);
    }

}
