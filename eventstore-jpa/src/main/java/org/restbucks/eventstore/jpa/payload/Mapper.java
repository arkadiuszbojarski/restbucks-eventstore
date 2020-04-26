package org.restbucks.eventstore.jpa.payload;

public interface Mapper<S> {
    S serialize(Object o);
    <T> T deserialize(S content, Class<T> type);
}
