package com.searchmetrics.audit.job.config.statistics;

import com.codahale.metrics.Metric;
import com.codahale.metrics.MetricRegistry;

import java.util.Map;

/**
 * Set of used metrics.
 */
public class Metrics {

    private final MetricRegistry registry;

    /**
     * Assign registry names for metrics
     *
     * @param registry
     */
    public Metrics(final MetricRegistry registry) {
        this.registry = registry;
    }

    public Map<String, Metric> getMetrics() {
        return this.registry.getMetrics();
    }
}
