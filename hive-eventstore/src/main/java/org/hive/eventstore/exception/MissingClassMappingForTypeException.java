package org.hive.eventstore.exception;

import static java.lang.String.format;

public class MissingClassMappingForTypeException extends HiveEventStoreException {
    private static final String MESSAGE = "Class mapping not found for type: '%s'";

    public static MissingClassMappingForTypeException missingClassMappingForType(String type) {
        return new MissingClassMappingForTypeException(type);
    }

    private MissingClassMappingForTypeException(String type) {
        super(format(MESSAGE, type));
    }
}
