package org.hive.eventstore.mapping;

import java.util.Optional;

public interface ClassMapper<T> {
    Optional<T> getTypeForClass(Class<?> typeClass);
}
