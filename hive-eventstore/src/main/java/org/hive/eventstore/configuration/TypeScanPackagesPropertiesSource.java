package org.hive.eventstore.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.*;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.Objects.requireNonNull;
import static org.springframework.util.ClassUtils.getPackageName;
import static org.springframework.util.StringUtils.isEmpty;

@Slf4j
public class TypeScanPackagesPropertiesSource {
    private static final String CANNOT_REGISTER_PACKAGES_PROPERTY_MESSAGE = "Cannot register base type packages property";
    private static final String DISCOVERED_PACKAGES_MESSAGE = "Discovered %d packages to scan";
    private static final String REGISTERED_PROPERTY_MESSAGE = "Registered property %s=%s";

    private static final String BASE_PACKAGES_PROPERTY_NAME = "restbucks.eventstore.basePackages";
    private static final String PROPERTY_SOURCE_NAME = "eventstore";

    static class Registrar implements ImportBeanDefinitionRegistrar {
        private final Optional<AbstractEnvironment> environment;

        public Registrar(Environment environment) {
            this.environment = instance(environment, AbstractEnvironment.class);
        }

        @Override
        public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
            final var packages = extractPackagesToScan(metadata);

            environment
                    .map(AbstractEnvironment::getPropertySources)
                    .ifPresentOrElse(
                            registerPackagesPropertySource(packages),
                            logRegistrationFailure());
        }
    }

    private static Set<String> extractPackagesToScan(AnnotationMetadata metadata) {
        final var annotationName = TypeScan.class.getName();
        final var attributes = AnnotationAttributes.fromMap(metadata.getAnnotationAttributes(annotationName));
        final var basePackages = attributes.getStringArray("basePackages");
        final var packagesToScan = asList(basePackages);

        if (noPackagesSpecified(packagesToScan)) {
            final var packageName = getPackageName(metadata.getClassName());
            Assert.state(!isEmpty(packageName), "@TypeScan cannot be used with the default package");
            packagesToScan.add(packageName);
        }

        final var packages = Set.copyOf(packagesToScan);
        logDiscoveredPackages(packages);

        return packages;
    }

    private static boolean noPackagesSpecified(List<String> packagesToScan) {
        return packagesToScan.isEmpty();
    }

    private static <T> Optional<T> instance(Object o, Class<T> requiredClass) {
        requireNonNull(o);
        return Optional
                .of(requireNonNull(requiredClass))
                .filter(c -> c.isInstance(o))
                .map(c -> c.cast(o));
    }

    private static Consumer<MutablePropertySources> registerPackagesPropertySource(Set<String> packages) {
        return sources -> {
            sources.addLast(basePackagesProperty(packages));
            logRegistrationSuccess(packages);
        };
    }

    private static PropertySource<?> basePackagesProperty(Set<String> packages) {
        return new MapPropertySource(PROPERTY_SOURCE_NAME, Map.of(BASE_PACKAGES_PROPERTY_NAME, packages));
    }

    private static void logRegistrationSuccess(Set<String> packages) {
        log.info(format(REGISTERED_PROPERTY_MESSAGE, BASE_PACKAGES_PROPERTY_NAME, packages));
    }
    private static Runnable logRegistrationFailure() {
        return () -> log.error(CANNOT_REGISTER_PACKAGES_PROPERTY_MESSAGE);
    }
    private static void logDiscoveredPackages(Set<String> packages) {
        log.info(format(DISCOVERED_PACKAGES_MESSAGE, packages.size(), packages));
    }
}
