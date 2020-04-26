package org.hive.eventstore.payload;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@RequiredArgsConstructor
public class JSONMapper implements Mapper<String> {
    private final ObjectMapper mapper;

    @Override
    @SneakyThrows
    public String serialize(Object o) {
        return o != null ? mapper.writeValueAsString(o) : null;
    }

    @Override
    @SneakyThrows
    public <T> T deserialize(String content, Class<T> type) {
        return mapper.readValue(content, type);
    }

}
