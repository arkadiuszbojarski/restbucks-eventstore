package org.hive.eventstore.stream;

import lombok.NonNull;
import lombok.Value;

@Value(staticConstructor = "of")
public class AttributeVO {
    public static AttributeVO empty(final String name) {
        return new AttributeVO(name, null, null);
    }

    @NonNull private final String name;
    private final String value;
    private final String type;
}
