package com.searchmetrics.audit.job.config;

import com.searchmetrics.audit.job.config.startstopmanager.BasicJavaApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 *
 */
@SpringBootApplication
public class DomainConfigVaadinApplication implements BasicJavaApplication {

    public static void main(String[] args) {
        SpringApplication.run(DomainConfigVaadinApplication.class);
    }

    @Override
    public void startApplication() {
        try {
            SpringApplication.run(DomainConfigVaadinApplication.class);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void stopApplication() {

    }

    @Override
    public String getApplicationName() {
        return "Custom Domain Config Service";
    }
}
