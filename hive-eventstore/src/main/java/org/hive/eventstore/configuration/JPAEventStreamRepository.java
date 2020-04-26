package org.hive.eventstore.configuration;

import org.hive.eventstore.stream.EventStream;
import org.hive.eventstore.stream.EventStreamRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface JPAEventStreamRepository extends EventStreamRepository, JpaRepository<EventStream, UUID> {
}
