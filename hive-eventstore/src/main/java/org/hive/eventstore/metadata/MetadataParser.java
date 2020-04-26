package org.hive.eventstore.metadata;

import lombok.RequiredArgsConstructor;
import org.hive.eventstore.api.EventMetadata;
import org.hive.eventstore.mapping.registry.ClassMappingRegistry;
import org.hive.eventstore.payload.Mapper;
import org.hive.eventstore.stream.AttributeVO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static java.util.stream.Collectors.toUnmodifiableList;
import static org.hive.eventstore.exception.MissingTypeMappingException.missingTypeMapping;

@Component
@RequiredArgsConstructor
public class MetadataParser implements Function<EventMetadata, List<AttributeVO>> {
    private final ClassMappingRegistry classRegistry;
    private final Mapper<String> mapper;

    @Override
    public List<AttributeVO> apply(EventMetadata metadata) {
        return metadata.keySet().stream()
                .map(name -> Optional
                        .ofNullable(metadata.get(name))
                        .map(createAttribute(name))
                        .orElse(AttributeVO.empty(name)))
                .collect(toUnmodifiableList());
    }

    private Function<Object, AttributeVO> createAttribute(String name) {
        return value -> {
            final var classType = extractType(value);
            final var serialized = mapper.serialize(value);
            final var attribute = AttributeVO.of(name, serialized, classType);

            return attribute;
        };
    }

    private String extractType(Object value) {
        final var valueClass = value.getClass();
        return classRegistry
                .getTypeForClass(valueClass)
                .orElseThrow(() -> missingTypeMapping(valueClass));
    }
}
