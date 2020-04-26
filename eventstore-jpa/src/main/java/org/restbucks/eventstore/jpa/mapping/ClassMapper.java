package org.restbucks.eventstore.jpa.mapping;

import java.util.Optional;

public interface ClassMapper<T> {
    Optional<T> getTypeForClass(Class<?> typeClass);
}
