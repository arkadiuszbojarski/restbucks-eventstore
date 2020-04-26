package org.restbucks.test.types;

import lombok.Value;
import org.restbucks.eventstore.jpa.annotation.TypeAlias;

@Value
@TypeAlias(type = "test.passed")
public class TestEvent {
    private final String value = "foo";
}
