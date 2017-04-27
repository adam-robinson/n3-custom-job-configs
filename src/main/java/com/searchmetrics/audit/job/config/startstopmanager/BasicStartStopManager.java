package com.searchmetrics.audit.job.config.startstopmanager;

/**
 *
 */
import org.apache.log4j.Logger;
import org.tanukisoftware.wrapper.WrapperListener;
import org.tanukisoftware.wrapper.WrapperManager;

/**
 * Basic start/stop manager for java service wrapper based applications. This class is used by the java service wrapper
 * to startup the application.
 */
public class BasicStartStopManager implements WrapperListener {

    private static final Logger LOGGER = Logger.getLogger(BasicStartStopManager.class.getName());
    private final BasicJavaApplication application;

    private static final int SIGNAL_STARTING = 60000;
    private static final int MAX_SHUTDOWN_TIME = 60000;

    /**
     * Constructor setting up the application to execute.
     *
     * @param application Application to execute.
     */
    public BasicStartStopManager(final BasicJavaApplication application) {
        this.application = application;
    }

    @Override
    public void controlEvent(int arg0) {
        if (arg0 != WrapperManager.WRAPPER_CTRL_LOGOFF_EVENT || !WrapperManager.isLaunchedAsService()) {
            WrapperManager.stop(0);
        }
    }

    @Override
    public Integer start(String[] args) {
        LOGGER.info(String.format("Starting: %s", application.getApplicationName()));
        WrapperManager.signalStarting(SIGNAL_STARTING);

        try {
            application.startApplication();
        } catch (Exception e) {
            LOGGER.error("Error starting application.", e);
            return 1;
        }
        LOGGER.info(String.format("Started: %s", application.getApplicationName()));
        return null;
    }

    @Override
    public int stop(int code) {
        LOGGER.info(String.format("Stopping: %s", application.getApplicationName()));
        WrapperManager.signalStopping(MAX_SHUTDOWN_TIME);
        application.stopApplication();
        LOGGER.info(String.format("Stopped: %s", application.getApplicationName()));
        WrapperManager.signalStopped(0);
        return 0;
    }
}
