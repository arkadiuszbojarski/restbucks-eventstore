package org.restbucks.eventstore.jpa.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface TypeAlias {
    String type();
}
