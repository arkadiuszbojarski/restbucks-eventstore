package org.hive.eventstore.payload;

public interface Mapper<S> {
    S serialize(Object o);
    <T> T deserialize(S content, Class<T> type);
}
