package org.restbucks.eventstore.jpa.mapping.registry;

import lombok.RequiredArgsConstructor;
import org.restbucks.eventstore.jpa.mapping.ClassMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ClassMappingRegistry {
    private final List<ClassMapper> mappings;

    // TODO: handle colliding class mappings
    public Optional<String> getTypeForClass(Class aClass) {
        return mappings.stream()
                .map(mapping -> mapping.getTypeForClass(aClass))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findAny();
    }
}
