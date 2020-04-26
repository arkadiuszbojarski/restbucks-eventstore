package org.hive.eventstore.api;

import static java.util.Objects.requireNonNull;

public enum MetadataAttribute {
    EVENT_ID("eventID"),
    STREAM_ID("streamID"),
    TIMESTAMP("timestamp"),
    TYPE("type");

    private final String attribute;

    MetadataAttribute(String attribute) {
        this.attribute = requireNonNull(attribute);
    }

    public String attribute() {
        return attribute;
    }

}
