package org.restbucks.eventstore.jpa.mapping.classname;

import org.restbucks.eventstore.jpa.mapping.ClassMapper;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ClassNameClassMapper implements ClassMapper<String> {

    @Override
    public Optional<String> getTypeForClass(Class<?> typeClass) {
        return Optional
                .ofNullable(typeClass)
                .map(Class::getName);
    }
}
