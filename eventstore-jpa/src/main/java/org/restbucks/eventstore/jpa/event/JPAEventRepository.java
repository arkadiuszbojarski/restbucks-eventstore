package org.restbucks.eventstore.jpa.event;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface JPAEventRepository extends JpaRepository<DomainEvent, UUID>, QuerydslPredicateExecutor<DomainEvent> {

}
