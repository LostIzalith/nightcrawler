package com.github.lostizalith.adcreator;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.Resource;

/**
 * Abstract class for initial context scanning.
 */
public abstract class AbstractAppContextInitializer {

    private static final String CONFIG = "classpath:config.properties";

    /**
     * Start application context scanning.
     *
     * @param basePackage is a package to scan
     */
    protected void start(final String basePackage) {
        final ApplicationContext context = createContext(basePackage);
        main(context);
    }

    private ApplicationContext createContext(final String basePackage) {
        final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        final Resource resource = context.getResource(CONFIG);
        context.addBeanFactoryPostProcessor(fetchPropertySourceConfigurer(resource));
        context.scan(basePackage);
        context.refresh();

        return context;
    }

    private static PropertySourcesPlaceholderConfigurer fetchPropertySourceConfigurer(final Resource... resources) {
        final PropertySourcesPlaceholderConfigurer propertySourceConfigurer = new PropertySourcesPlaceholderConfigurer();
        propertySourceConfigurer.setLocations(resources);

        return propertySourceConfigurer;
    }

    /**
     * Method to start application.
     *
     * @param context is an application context
     */
    protected abstract void main(ApplicationContext context);
}
