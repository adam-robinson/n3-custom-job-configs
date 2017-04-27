package com.searchmetrics.audit.job.config;

/**
 *
 */
import com.searchmetrics.audit.job.config.startstopmanager.BasicStartStopManager;
import org.tanukisoftware.wrapper.WrapperManager;

/**
 * Concrete start/stop manager for the application
 */
public class StartStopManager extends BasicStartStopManager {

    /**
     * Constructor setting up the application to execute.
     *
     * @param application   Application to execute.
     */
    public StartStopManager(final DomainConfigVaadinApplication application) {
        super(application);
    }

    /**
     * Main method.
     *
     * @param args  CLI parameters.
     */
    public static void main(String[] args) {
        // App-class for the hello world application
        // App-class for the micro service stuff
        final DomainConfigVaadinApplication application = new DomainConfigVaadinApplication();
        final StartStopManager startStopManager = new StartStopManager(application);
        WrapperManager.start(startStopManager, args);
    }
}
