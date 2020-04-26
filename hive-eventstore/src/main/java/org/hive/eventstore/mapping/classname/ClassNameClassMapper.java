package org.hive.eventstore.mapping.classname;

import org.hive.eventstore.mapping.ClassMapper;
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
