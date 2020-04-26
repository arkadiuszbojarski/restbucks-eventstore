package org.restbucks.test;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.restbucks.eventstore.EventStore;
import org.restbucks.eventstore.event.Event;
import org.restbucks.eventstore.metadata.EventMetadata;
import org.restbucks.test.types.TestEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TestApplication.class)
public class EventStoreJPATest {

    @Autowired
    private EventStore store;

    @Test
    public void shouldStoreAndRetrieveTestEvent() {
        // given:
        final var streamID = randomUUID();
        final var payload = new TestEvent();
        final var metadata = EventMetadata.builder().build();

        // when:
        store.appendEventToStream(streamID, Event.createEvent(payload, metadata));

        // then:
        then(store.findEventsOfStream(streamID))
                .hasSize(1)
                .allSatisfy(event -> assertThat(event.payload())
                        .isInstanceOfSatisfying(
                                TestEvent.class,
                                testEvent -> assertThat(testEvent.getValue()).isEqualTo("foo")));

        store.findEventsWithMetadata(EventMetadata.builder().attribute("streamID", streamID).build());
    }
}
