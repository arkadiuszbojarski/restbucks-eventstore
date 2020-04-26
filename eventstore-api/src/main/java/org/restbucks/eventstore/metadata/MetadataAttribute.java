package org.restbucks.eventstore.metadata;

import java.util.Set;

import static java.util.Arrays.stream;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toUnmodifiableSet;

public enum MetadataAttribute {
    EVENT_ID("eventID"),
    STREAM_ID("streamID"),
    TIMESTAMP("timestamp"),
    TYPE("type", true);

    public static final Set<String> MODIFIABLE_ATTRIBUTES = stream(MetadataAttribute.values())
            .filter(MetadataAttribute::modifiable)
            .map(MetadataAttribute::attribute)
            .collect(toUnmodifiableSet());

    private final String attribute;
    private final boolean modifiable;

    MetadataAttribute(String attribute, boolean modifiable) {
        this.attribute = requireNonNull(attribute);
        this.modifiable = requireNonNull(modifiable);
    }

    MetadataAttribute(String attribute) {
        this(attribute, false);
    }

    public String attribute() {
        return attribute;
    }
    public boolean modifiable() {
        return modifiable;
    }

}
