package org.restbucks.eventstore.jpa.mapping;

import java.util.Optional;

public interface ClassMapper {
    Optional<String> getTypeForClass(Class<?> typeClass);
}
