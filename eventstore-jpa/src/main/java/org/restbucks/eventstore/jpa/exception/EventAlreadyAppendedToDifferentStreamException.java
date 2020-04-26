package org.restbucks.eventstore.jpa.exception;

import java.util.UUID;

import static java.lang.String.format;

public class EventAlreadyAppendedToDifferentStreamException extends EventstoreException {
    private static final String MESSAGE_TEMPLATE = "Event: %s already belongs to stream: %s and cannot append to stream: %s.";

    public static EventAlreadyAppendedToDifferentStreamException eventAlreadyAppendedToDifferentStream(
            UUID eventID,
            UUID currentEventStreamID,
            UUID attemptedStreamID) {
        return new EventAlreadyAppendedToDifferentStreamException(eventID, currentEventStreamID, attemptedStreamID);
    }

    public EventAlreadyAppendedToDifferentStreamException(UUID eventID, UUID currentEventStreamID, UUID attemptedStreamID) {
        super(format(MESSAGE_TEMPLATE, eventID, currentEventStreamID, attemptedStreamID));
    }
}
