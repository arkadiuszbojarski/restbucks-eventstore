package org.restbucks.eventstore.jpa.stream;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Repository
@Transactional
@RequiredArgsConstructor
public class StreamRepository {
    private final JPAEventStreamRepository streamRepository;

    public EventStream findStreamByID(final UUID streamID) {
        return streamRepository
                .findById(streamID)
                .orElseGet(() -> EventStream.of(streamID));
    }

    public void save(EventStream stream) {
        streamRepository.save(stream);
    }
}
