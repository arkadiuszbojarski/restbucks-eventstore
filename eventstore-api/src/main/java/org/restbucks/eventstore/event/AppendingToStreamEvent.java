package org.restbucks.eventstore.event;

import lombok.NonNull;
import lombok.Value;

import java.util.UUID;

@Value(staticConstructor = "of")
public final class AppendingToStreamEvent {
    @NonNull private final UUID streamID;
    @NonNull private final UUID eventID;
}
