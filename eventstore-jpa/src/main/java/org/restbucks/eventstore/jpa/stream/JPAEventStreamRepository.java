package org.restbucks.eventstore.jpa.stream;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface JPAEventStreamRepository extends JpaRepository<EventStream, UUID> {
}
