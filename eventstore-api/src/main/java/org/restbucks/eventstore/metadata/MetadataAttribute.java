package org.restbucks.eventstore.metadata;

import java.util.Set;

import static java.util.Arrays.stream;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toUnmodifiableSet;

public enum MetadataAttribute {
    EVENT_ID("eventID"),
    STREAM_ID("streamID"),
    TIMESTAMP("timestamp"),
    TYPE("type", false);

    public static final Set<String> UNMODIFIABLE_ATTRIBUTES = stream(MetadataAttribute.values())
            .filter(MetadataAttribute::unmodifiable)
            .map(MetadataAttribute::attribute)
            .collect(toUnmodifiableSet());

    private final String attribute;
    private final boolean unmodifiable;

    MetadataAttribute(String attribute, boolean unmodifiable) {
        this.attribute = requireNonNull(attribute);
        this.unmodifiable = requireNonNull(unmodifiable);
    }

    MetadataAttribute(String attribute) {
        this(attribute, true);
    }

    public String attribute() {
        return attribute;
    }
    public boolean unmodifiable() {
        return unmodifiable;
    }

}
