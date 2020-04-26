package org.restbucks.eventstore.jpa.exception;

import static java.lang.String.format;

public class ConflictingTypeMappingException extends EventstoreException {
    private static final String MESSAGE_TEMPLATE = "Class %s and class %s where mapped to same type %s.";

    public static ConflictingTypeMappingException conflictingTypeMapping(
            Class aClass,
            Class bClass,
            String type) {
        return new ConflictingTypeMappingException(aClass, bClass, type);
    }

    public ConflictingTypeMappingException(Class aClass, Class bClass, String type) {
        super(format(MESSAGE_TEMPLATE, aClass, bClass, type));
    }
}
