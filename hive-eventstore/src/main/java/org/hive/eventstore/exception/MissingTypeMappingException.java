package org.hive.eventstore.exception;

import static java.lang.String.format;

public class MissingTypeMappingException extends HiveEventStoreException {
    private static final String MESSAGE = "Could not determine type mapping for: '%s'";

    public static MissingTypeMappingException missingTypeMapping(Object object) {
        return new MissingTypeMappingException(object);
    }

    private MissingTypeMappingException(Object object) {
        super(format(MESSAGE, object));
    }
}
