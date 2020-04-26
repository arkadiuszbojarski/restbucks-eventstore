package org.restbucks.eventstore.jpa.mapping.classname;

import lombok.extern.slf4j.Slf4j;
import org.restbucks.eventstore.jpa.mapping.TypeMapper;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
public class ClassNameTypeMapper implements TypeMapper {

    private static Class<?> classForName(String s) {
        try {
            return Class.forName(s);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    @Override
    public Optional<Class<?>> getClassForType(String classType) {
        return Optional.ofNullable(classType).map(ClassNameTypeMapper::classForName);
    }
}