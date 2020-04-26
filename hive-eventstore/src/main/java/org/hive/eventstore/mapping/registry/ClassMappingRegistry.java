package org.hive.eventstore.mapping.registry;

import lombok.RequiredArgsConstructor;
import org.hive.eventstore.mapping.ClassMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ClassMappingRegistry {
    private final List<ClassMapper<String>> mappings;

    // TODO: handle colliding class mappings
    public Optional<String> getTypeForClass(Class aClass) {
        return mappings.stream()
                .map(mapping -> mapping.getTypeForClass(aClass))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findAny();
    }
}
