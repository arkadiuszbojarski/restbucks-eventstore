package org.restbucks.test.types;

import lombok.Data;
import org.hive.eventstore.annotation.TypeAlias;

@Data
@TypeAlias(type = "test.passed")
public class TestEvent {
    public static TestEvent of(String value) {
        final var event = new TestEvent();
        event.setValue(value);

        return event;
    }

    private String value;
}
