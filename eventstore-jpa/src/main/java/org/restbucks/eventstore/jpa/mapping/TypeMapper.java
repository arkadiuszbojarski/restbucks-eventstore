package org.restbucks.eventstore.jpa.mapping;

import java.util.Optional;

public interface TypeMapper {
    Optional<Class<?>> getClassForType(String classType);
}
