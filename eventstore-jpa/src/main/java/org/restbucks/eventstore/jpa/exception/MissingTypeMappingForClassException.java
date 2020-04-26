package org.restbucks.eventstore.jpa.exception;

import static java.lang.String.format;

public class MissingTypeMappingForClassException extends RuntimeException{
    private static final String MESSAGE = "Class mapping not found for type: '%s'";

    public static MissingTypeMappingForClassException missingTypeMappingForClass(Class aClass) {
        return new MissingTypeMappingForClassException(aClass);
    }

    private MissingTypeMappingForClassException(Class aClass) {
        super(format(MESSAGE, aClass));
    }
}
