package org.hive.eventstore.mapping.annotation;

import org.hive.eventstore.mapping.ClassMapper;

import java.lang.annotation.Annotation;
import java.util.Optional;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;

public class AnnotationClassMapper<A extends Annotation, T> implements ClassMapper<T> {
    private final Function<A, T> extractor;
    private final Class<A> annotation;

    public static <A extends Annotation, T> AnnotationClassMapper<A, T> annotationTypeMapper(
            Class<A> annotation,
            Function<A, T> typeExtractor) {
        return new AnnotationClassMapper<>(annotation, typeExtractor);
    }

    private AnnotationClassMapper(Class<A> annotation, Function<A, T> extractor) {
        this.annotation = requireNonNull(annotation);
        this.extractor = requireNonNull(extractor);
    }

    @Override
    public Optional<T> getTypeForClass(final Class<?> typeClass) {
        return Optional
                .ofNullable(typeClass)
                .map(t -> t.getAnnotation(this.annotation))
                .map(extractor);
    }

    public Class<A> annotation() {
        return annotation;
    }

}
