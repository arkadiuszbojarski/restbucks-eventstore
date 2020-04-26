package org.hive.eventstore.mapping;

import java.util.Optional;

public interface TypeMapper<T> {
    Optional<Class<?>> getClassForType(T classType);
}
