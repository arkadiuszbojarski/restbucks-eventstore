package org.hive.eventstore.configuration;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.hive.eventstore.event.EventDTO;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Mapper
public interface MyBatisEventRepository {
    List<EventDTO> findEventsByStreamId(@Param("streamID") UUID streamID);

    List<EventDTO> findEventsWithMetadata(@Param("metadata") Map<String, String> metadata, @Param("match") Integer match);

    Optional<EventDTO> findById(@Param("eventID") UUID eventID);
}
