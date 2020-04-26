package org.hive.eventstore.api;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.*;

import static java.util.Collections.unmodifiableCollection;
import static java.util.Collections.unmodifiableSet;
import static java.util.Objects.requireNonNull;

public class EventMetadata implements Map<String, Object> {
    public static final EventMetadata EMPTY = EventMetadata.builder().build();

    public static final EventMetadata empty() {
        return EMPTY;
    }

    public static MetadataBuilder builder() {
        return new MetadataBuilder();
    }

    private final Map<String, Object> attributes = new HashMap<>();

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
        return get(MetadataAttribute.STREAM_ID.attribute());
    }

    public UUID getEventID() {
        return get(MetadataAttribute.EVENT_ID.attribute());
    }

    public String getType() {
        return get(MetadataAttribute.TYPE.attribute());
    }

    public Instant getTimestamp() {
        return get(MetadataAttribute.TIMESTAMP.attribute());
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
            attributes.put(MetadataAttribute.EVENT_ID.attribute(), eventID);

            return this;
        }

        public MetadataBuilder timestamp(final Instant timestamp) {
            requireNonNull(timestamp);
            attributes.put(MetadataAttribute.TIMESTAMP.attribute(), timestamp);

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
