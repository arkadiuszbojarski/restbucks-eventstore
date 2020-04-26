package org.restbucks.eventstore.metadata;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.*;

import static java.util.Collections.unmodifiableCollection;
import static java.util.Collections.unmodifiableSet;
import static java.util.Objects.requireNonNull;
import static org.restbucks.eventstore.metadata.MetadataAttribute.*;

public class EventMetadata implements Map<String, Object> {
    private final Map<String, Object> attributes = new HashMap<>();

    public static MetadataBuilder builder() {
        return new MetadataBuilder();
    }

    private EventMetadata(final Map<String, Object> attributes) {
        requireNonNull(attributes);

        for (String name : attributes.keySet()) {
            final var value = attributes.get(name);
            this.attributes.put(name, value);
        }
    }

    @Override
    public int size() {
        return attributes.size();
    }

    @Override
    public boolean isEmpty() {
        return attributes.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return attributes.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return attributes.containsValue(value);
    }

    public <T> T get(String attribute) {
        requireNonNull(attribute);
        final var value = (T) attributes.get(attribute);

        return value;
    }

    public UUID getStreamID() {
        return get(STREAM_ID.attribute());
    }

    public UUID getEventID() {
        return get(EVENT_ID.attribute());
    }

    public <T> T getType(Class<T> typeClass) {
        return get(TYPE.attribute());
    }

    public String getType() {
        return getType(String.class);
    }

    public Instant getTimestamp() {
        return get(TIMESTAMP.attribute());
    }

    @Override
    public Object get(Object key) {
        return attributes.get(key);
    }

    @Override
    public Object put(String key, Object value) {
        throw new UnsupportedOperationException("Event metadata is immutable");
    }

    @Override
    public Object remove(Object key) {
        throw new UnsupportedOperationException("Event metadata is immutable");
    }

    @Override
    public void putAll(Map<? extends String, ?> m) {
        throw new UnsupportedOperationException("Event metadata is immutable");
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("Event metadata is immutable");
    }

    @Override
    public Set<String> keySet() {
        return unmodifiableSet(attributes.keySet());
    }

    @Override
    public Collection<Object> values() {
        return unmodifiableCollection(attributes.values());
    }

    @Override
    public Set<Entry<String, Object>> entrySet() {
        return unmodifiableSet(attributes.entrySet());
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class MetadataBuilder {
        private final Map<String, Object> attributes = new HashMap<>();

        public MetadataBuilder eventID(final UUID eventID) {
            requireNonNull(eventID);
            attributes.put(EVENT_ID.attribute(), eventID);

            return this;
        }

        public MetadataBuilder timestamp(final Instant timestamp) {
            requireNonNull(timestamp);
            attributes.put(TIMESTAMP.attribute(), timestamp);

            return this;
        }

        public MetadataBuilder attribute(final String name, final Object value) {
            requireNonNull(name);
            attributes.put(name, value);

            return this;
        }

        public EventMetadata build() {
            return new EventMetadata(attributes);
        }
    }
}
