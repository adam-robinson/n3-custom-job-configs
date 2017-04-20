package com.searchmetrics.audit.job.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Created by arobinson on 3/8/17.
 */
@Configuration
@ComponentScan("com.searchmetrics.audit.job.config.dao")
@PropertySource(name = "appProps", value = "classpath:application.properties")
public class SpringConfig {
}
