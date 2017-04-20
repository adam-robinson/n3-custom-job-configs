package com.searchmetrics.audit.job.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.searchmetrics.audit.job.config.api.rest.AuditJobConfigEndpoint;
import com.searchmetrics.audit.job.config.api.rest.ExceptionMappers;
import com.searchmetrics.audit.job.config.dao.DomainConfigRepository;
import io.dropwizard.Application;
import io.dropwizard.jersey.jackson.JacksonMessageBodyProvider;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.federecio.dropwizard.swagger.SwaggerBundle;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * App starting the micro service environment.
 */
public class AuditJobConfigApp extends Application<AuditJobConfigConfiguration> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuditJobConfigApp.class.getName());
    private final AnnotationConfigApplicationContext applicationContext;

    /**
     * main method
     *
     * @param args
     */
    public static void main(String[] args) throws Exception {
        new AuditJobConfigApp().run(args);
    }

    public AuditJobConfigApp() {
        super();
        this.applicationContext = new AnnotationConfigApplicationContext(SpringConfig.class);
    }

    public void startApplication() throws ExceptionInInitializerError {
        try {
            run(new String[]{"server", System.getProperty("config.file")});
        } catch(Exception e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public void stopApplication() {
        //stop app
    }

    public String getApplicationName() {
        return "audit-job-config service";
    }

    @Override
    public String getName() {
        return "audit-job-config service";
    }

    @Override
    public void initialize(Bootstrap<AuditJobConfigConfiguration> bootstrap) {
        LOGGER.info("Bootstrapping application...");

        bootstrap.addBundle(new SwaggerBundle<AuditJobConfigConfiguration>() {
            @Override
            protected SwaggerBundleConfiguration getSwaggerBundleConfiguration(AuditJobConfigConfiguration configuration) {
                return configuration.swaggerBundleConfiguration;
            }
        });
    }

    @Override
    public void run(AuditJobConfigConfiguration auditJobConfigConfiguration, Environment environment) throws Exception {
        final ObjectMapper mapper = new ObjectMapper();

        final DomainConfigRepository domainConfigRepository = applicationContext.getBean(DomainConfigRepository.class);
        final AuditJobConfigEndpoint auditJobConfigEndpoint = new AuditJobConfigEndpoint(domainConfigRepository);
        environment.jersey().register(ExceptionMappers.GenericExceptionMapper.class);
        environment.jersey().register(ExceptionMappers.IllegalArgumentExceptionMapper.class);
        environment.jersey().register(new JacksonMessageBodyProvider(mapper));
        environment.jersey().register(auditJobConfigEndpoint);
    }
}
