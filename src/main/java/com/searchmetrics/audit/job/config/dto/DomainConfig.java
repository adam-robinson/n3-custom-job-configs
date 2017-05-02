package com.searchmetrics.audit.job.config.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.cassandra.core.PrimaryKeyType;
import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.mapping.Table;

import java.util.List;
import java.util.UUID;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.apache.commons.lang3.EnumUtils.isValidEnum;
@Table(value = "domainConfigs")
public class DomainConfig {
    public enum ProxyType {NONE, DE, US}

    public enum RuleScope {ALL, KEYWORD, SO, SSO}

    public enum UserAgentType {
        SEARCHMETRICSBOT,
        GOOGLEBOT
    }
    @PrimaryKeyColumn(
        name = "uuid",
        ordinal = 0,
        type = PrimaryKeyType.PARTITIONED)
    private final UUID uuid;
    @PrimaryKeyColumn(
            name = "domain",
            ordinal = 1,
            type = PrimaryKeyType.CLUSTERED)
    private final String domain; //default crawler config
    @Column
    private Boolean exactMatch = true;
    @Column
    private Boolean calculateBaseURL = false;
    @Column
    private List<String> customHeaders;
    @Column
    private Boolean isJavascriptCrawl = false;
    @Column
    private Boolean isStaticIPCrawl = false;
    @Column
    private Boolean noCanonical = false;
    @Column
    private Boolean noCookies = false;
    @Column
    private String proxyType = ProxyType.NONE.name();
    @Column
    private Integer readTimeout = 20;
    @Column
    private String ruleScope = RuleScope.ALL.name();
    @Column
    private String userAgent = UserAgentType.SEARCHMETRICSBOT.name();
    @Column
    private List<String> notes;

    @JsonCreator
    public DomainConfig(
        @JsonProperty("domain") final String domain,
        @JsonProperty("exactMatch") final Boolean exactMatch,
        @JsonProperty("calculateBaseURL") final Boolean calculateBaseURL,
        @JsonProperty("customHeaders") final List<String> customHeaders,
        @JsonProperty("isJavascriptCrawl") final Boolean isJavascriptCrawl,
        @JsonProperty("isStaticIPCrawl") final Boolean isStaticIPCrawl,
        @JsonProperty("noCanonical") final Boolean noCanonical,
        @JsonProperty("noCookies") final Boolean noCookies,
        @JsonProperty("notes") final List<String> notes,
        @JsonProperty("proxyType") final String proxyType,
        @JsonProperty("readTimeout") final Integer readTimeout,
        @JsonProperty("ruleScope") final String ruleScope,
        @JsonProperty("userAgent") final String userAgent) {
        this.domain = checkNotNull(domain);

        checkNotNull(ruleScope);
        if (isValidEnum(RuleScope.class, ruleScope))
            this.ruleScope = ruleScope;


        if (null != exactMatch)
            this.exactMatch = exactMatch;

        if (null != calculateBaseURL)
            this.calculateBaseURL = calculateBaseURL;

        if (null != customHeaders)
            this.customHeaders = customHeaders;

        if (null != noCanonical)
            this.noCanonical = noCanonical;

        if (null != noCookies)
            this.noCookies = noCookies;

        if (null != proxyType && isValidEnum(ProxyType.class, proxyType)) {
            this.proxyType = proxyType;
        }
        if (null != readTimeout)
            this.readTimeout = readTimeout;

        if (null != userAgent && isValidEnum(UserAgentType.class, userAgent)) {
            this.userAgent = userAgent;
        }

        if (null != notes)
            this.notes = notes;

        this.uuid = UUID.fromString(String.join("|", domain, exactMatch.toString(), userAgent));
    }

    @JsonProperty
    public UUID getUuid() {
        return uuid;
    }
    @JsonProperty // gets config data
    public String getDomain() {
        return domain;
    }
    @JsonProperty
    public Boolean getExactMatch() {
        return exactMatch;
    }
    @JsonProperty
    public Boolean getCalculateBaseURL() {
        return calculateBaseURL;
    }
    @JsonProperty
    public List<String> getCustomHeaders() {
        return customHeaders;
    }
    @JsonProperty
    public Boolean getNoCanonical() {
        return noCanonical;
    }
    @JsonProperty
    public Boolean getNoCookies() {
        return noCookies;
    }
    @JsonProperty
    public String getProxyType() {
        return proxyType;
    }
    @JsonProperty
    public Integer getReadTimeout() {
        return readTimeout;
    }
    @JsonProperty
    public String getUserAgent() {
        return userAgent;
    }
    @JsonProperty
    public List<String> getNotes() {
        return notes;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DomainConfig that = (DomainConfig) o;

        if (!uuid.equals(that.uuid))
            return false;
        if (! domain.equals(that.domain))
            return false;
        if (! exactMatch.equals(that.exactMatch))
            return false;
        if (! calculateBaseURL.equals(that.calculateBaseURL))
            return false;
        if (customHeaders != null ? ! customHeaders.equals(that.customHeaders) : that.customHeaders != null)
            return false;
        if (! noCanonical.equals(that.noCanonical))
            return false;
        if (! noCookies.equals(that.noCookies))
            return false;
        if (! proxyType.equals(that.proxyType))
            return false;
        if (! readTimeout.equals(that.readTimeout))
            return false;
        if (! userAgent.equals(that.userAgent))
            return false;
        return notes != null ? notes.equals(that.notes) : that.notes == null;
    }

    @Override
    public int hashCode() {
        int result = uuid.hashCode();
        result = 31 * result + domain.hashCode();
        result = 31 * result + exactMatch.hashCode();
        result = 31 * result + calculateBaseURL.hashCode();
        result = 31 * result + (customHeaders != null ? customHeaders.hashCode() : 0);
        result = 31 * result + noCanonical.hashCode();
        result = 31 * result + noCookies.hashCode();
        result = 31 * result + proxyType.hashCode();
        result = 31 * result + readTimeout.hashCode();
        result = 31 * result + userAgent.hashCode();
        result = 31 * result + (notes != null ? notes.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "DomainConfig{" +
                "uuid=" + uuid +
                ", domain='" + domain + '\'' +
                ", exactMatch=" + exactMatch +
                ", calculateBaseURL=" + calculateBaseURL +
                ", customHeaders=" + customHeaders +
                ", noCanonical=" + noCanonical +
                ", noCookies=" + noCookies +
                ", proxyType='" + proxyType + '\'' +
                ", readTimeout=" + readTimeout +
                ", userAgent='" + userAgent + '\'' +
                ", notes=" + notes +
                '}';
    }
}