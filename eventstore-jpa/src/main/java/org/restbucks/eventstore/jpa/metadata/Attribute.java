package org.restbucks.eventstore.jpa.metadata;

import lombok.*;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@Embeddable
@Setter(AccessLevel.NONE)
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Attribute implements Serializable {
    @NonNull private String name;
    private String value;
    private String type;

    public static Attribute empty(String name) {
        return Attribute.of(name, null, null);
    }
}
