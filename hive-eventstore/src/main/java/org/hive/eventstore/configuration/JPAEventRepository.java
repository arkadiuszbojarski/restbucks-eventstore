package org.hive.eventstore.configuration;

import org.hive.eventstore.event.DomainEvent;
import org.hive.eventstore.event.EventRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface JPAEventRepository extends JpaRepository<DomainEvent, UUID>, EventRepository {

}
