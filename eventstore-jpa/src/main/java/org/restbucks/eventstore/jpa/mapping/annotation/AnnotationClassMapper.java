package org.restbucks.eventstore.jpa.mapping.annotation;

import org.restbucks.eventstore.jpa.mapping.ClassMapper;

import java.lang.annotation.Annotation;
import java.util.Optional;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;

public class AnnotationClassMapper<A extends Annotation> implements ClassMapper {
    private final Function<A, String> extractor;
    private final Class<A> annotation;

    public static <A extends Annotation> AnnotationClassMapper<A> annotationTypeMapper(
            Class<A> annotation,
            Function<A, String> typeExtractor) {
        return new AnnotationClassMapper<>(annotation, typeExtractor);
    }

    private AnnotationClassMapper(Class<A> annotation, Function<A, String> extractor) {
        this.annotation = requireNonNull(annotation);
        this.extractor = requireNonNull(extractor);
    }

    @Override
    public Optional<String> getTypeForClass(final Class<?> typeClass) {
        requireNonNull(typeClass);
        final var annotation = typeClass.getAnnotation(this.annotation);

        return Optional
                .ofNullable(annotation)
                .map(extractor);
    }

    public Class<A> annotation() {
        return annotation;
    }
}
