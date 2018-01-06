package com.github.nightcrawler;

import org.springframework.context.ApplicationContext;

/**
 * Ad creator main class.
 */
public class AdCreatorAbstractApp extends AbstractAppContextInitializer {

    /**
     * Method to start tool.
     *
     * @param args are console arguments.
     */
    public static void main(final String[] args) {
        new AdCreatorAbstractApp().start(AdCreatorAbstractApp.class.getPackage().getName());
    }

    @Override
    protected void main(final ApplicationContext context) {
        //TODO: some business
    }
}
