package com.searchmetrics.audit.job.config.statistics;

/**
 * N3 service related configuration data
 */
public class N3ServiceConf {
    private String url;
    private String maxConnections;
    private String connectionTimeout;
    private String readTimeout;

    public String getUrl() {
        return url;
    }

    public Integer getMaxConnections() {
        return Integer.parseInt(maxConnections);
    }

    public Integer getConnectionTimeout() {
        return Integer.parseInt(connectionTimeout);
    }

    public Integer getReadTimeout() {
        return Integer.parseInt(readTimeout);
    }
}
