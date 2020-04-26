package org.hive.eventstore.event;

import lombok.Data;

import java.util.List;

@Data
public class EventDTO {
    private String eventID;
    private String type;
    private String payload;
    private List<AttributeDTO> attributes;
}
