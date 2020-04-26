package org.hive.eventstore.stream;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.List;
import java.util.UUID;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DomainEventVO {

    public static DomainEventVO of(final UUID eventID,
                                   final String payload,
                                   final String type,
                                   final List<AttributeVO> attributes) {
        return new DomainEventVO(eventID, payload, type, List.copyOf(attributes));
    }

    private final UUID eventID;
    private final String payload;
    private final String type;
    private final List<AttributeVO> attributes;
}
