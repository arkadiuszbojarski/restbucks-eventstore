package org.hive.eventstore.mapping.registry;

import lombok.RequiredArgsConstructor;
import org.hive.eventstore.mapping.TypeMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TypeMappingRegistry {
    private final List<TypeMapper<String>> mappings;

    // TODO: handle colliding type mappings
    public Optional<Class<?>> getClassForType(String type) {
        return mappings.stream()
                .map(mapping -> mapping.getClassForType(type))
                .filter(Optional::isPresent)
                .<Class<?>>map(Optional::get)
                .findAny();
    }
}
