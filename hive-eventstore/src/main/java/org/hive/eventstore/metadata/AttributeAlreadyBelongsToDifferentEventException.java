package org.hive.eventstore.metadata;

import org.hive.eventstore.exception.HiveEventStoreException;

import java.util.UUID;

import static java.lang.String.format;

public class AttributeAlreadyBelongsToDifferentEventException extends HiveEventStoreException {
    private static final String MESSAGE_TEMPLATE = "Attribute: %s already belongs to event: %s and cannot append to event: %s.";

    public static AttributeAlreadyBelongsToDifferentEventException attributeAlreadyBelongsToDifferentEvent(
            Integer attributeID,
            UUID currentEventID,
            UUID attemptedEventID) {
        return new AttributeAlreadyBelongsToDifferentEventException(attributeID, currentEventID, attemptedEventID);
    }

    public AttributeAlreadyBelongsToDifferentEventException(Integer attributeID, UUID currentEventID, UUID attemptedEventID) {
        super(format(MESSAGE_TEMPLATE, attributeID, currentEventID, attemptedEventID));
    }
}
