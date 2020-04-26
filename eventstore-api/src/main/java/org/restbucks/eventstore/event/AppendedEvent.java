package org.restbucks.eventstore.event;

import lombok.NonNull;
import lombok.Value;

import java.util.UUID;

@Value(staticConstructor = "of")
public final class AppendedEvent {
    @NonNull private final UUID streamID;
    @NonNull private final UUID eventID;
}
