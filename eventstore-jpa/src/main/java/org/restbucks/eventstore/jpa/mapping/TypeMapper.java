package org.restbucks.eventstore.jpa.mapping;

import java.util.Optional;

public interface TypeMapper<T> {
    Optional<Class<?>> getClassForType(T classType);
}
