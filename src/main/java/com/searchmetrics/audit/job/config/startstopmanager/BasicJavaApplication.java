package com.searchmetrics.audit.job.config.startstopmanager;

/**
 * Interface of the application that should run inside a java service wrapper object.
 */
public interface BasicJavaApplication {
    /**
     * Start the application.
     */
    void startApplication();

    /**
     * Stop the application.
     */
    void stopApplication();

    /**
     * Get the name of the application.
     *
     * @return Application-name.
     */
    String getApplicationName();
}