package org.hive.eventstore.event;

import lombok.Data;

@Data
public class AttributeDTO {
    private String name;
    private String value;
    private String type;
}
