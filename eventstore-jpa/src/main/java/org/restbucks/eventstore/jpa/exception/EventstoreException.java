package org.restbucks.eventstore.jpa.exception;

public class EventstoreException extends RuntimeException {
    public EventstoreException(String message) {
        super(message);
    }
}
