package org.hive.eventstore.event;

import org.hive.eventstore.exception.HiveEventStoreException;

import java.util.UUID;

import static java.lang.String.format;

public class EventAlreadyAppendedToDifferentStreamException extends HiveEventStoreException {
    private static final String MESSAGE_TEMPLATE = "Event: %s already belongs to stream: %s and cannot append to stream: %s.";

    public static EventAlreadyAppendedToDifferentStreamException eventAlreadyAppendedToDifferentStream(
            UUID eventID,
            UUID currentEventStreamID,
            UUID attemptedStreamID) {
        return new EventAlreadyAppendedToDifferentStreamException(eventID, currentEventStreamID, attemptedStreamID);
    }

    private EventAlreadyAppendedToDifferentStreamException(UUID eventID, UUID currentEventStreamID, UUID attemptedStreamID) {
        super(format(MESSAGE_TEMPLATE, eventID, currentEventStreamID, attemptedStreamID));
    }
}
