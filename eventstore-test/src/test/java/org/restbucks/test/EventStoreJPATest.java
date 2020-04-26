package org.restbucks.test;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.hive.eventstore.api.EventStore;
import org.hive.eventstore.api.Event;
import org.hive.eventstore.api.EventMetadata;
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

        // when:
        store.appendEventsToStream(streamID, Event.createEvent(TestEvent.of("foo"), EventMetadata.builder()
                .attribute("foo", "foo")
                .attribute("bar", true)
                .build()));

        store.appendEventsToStream(streamID, Event.createEvent(TestEvent.of("foo"), EventMetadata.builder()
                .attribute("foo", "foo")
                .build()));

        store.appendEventsToStream(streamID, Event.createEvent(TestEvent.of("foo"), EventMetadata.builder()
                .attribute("bar", true)
                .build()));

        store.appendEventsToStream(streamID, Event.createEvent(TestEvent.of("bar"), EventMetadata.builder()
                .attribute("foo", "bar")
                .build()));

        // then:
        then(store.findEventsOfStream(streamID)).hasSize(4);
        then(store.findEventsWithMetadata(EventMetadata.builder()
                .attribute("foo", "foo")
                .build()))
                .hasSize(2)
                .allSatisfy(event -> assertThat(event.payload())
                        .isInstanceOfSatisfying(TestEvent.class,
                                testEvent -> assertThat(testEvent.getValue()).isEqualTo("foo")));

        then(store.findEventsWithMetadata(EventMetadata.builder()
                .attribute("bar", true)
                .build()))
                .hasSize(2)
                .allSatisfy(event -> assertThat(event.payload())
                        .isInstanceOfSatisfying(TestEvent.class,
                                testEvent -> assertThat(testEvent.getValue()).isEqualTo("foo")));

        then(store.findEventsWithMetadata(EventMetadata.builder()
                .attribute("bar", true)
                .attribute("foo", "foo")
                .build()))
                .hasSize(1)
                .allSatisfy(event -> assertThat(event.payload())
                        .isInstanceOfSatisfying(TestEvent.class,
                                testEvent -> assertThat(testEvent.getValue()).isEqualTo("foo")));

        then(store.findEventsWithMetadata(EventMetadata.builder()
                .attribute("foo", "bar")
                .build()))
                .hasSize(1)
                .allSatisfy(event -> assertThat(event.payload())
                        .isInstanceOfSatisfying(TestEvent.class,
                                testEvent -> assertThat(testEvent.getValue()).isEqualTo("bar")));

    }
}
