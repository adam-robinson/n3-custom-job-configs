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

    public enum StaticIPCrawlType {NOT, DE, US}

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
    private String domain; //default crawler config
    @Column
    private Boolean exactMatch = true;
    @Column
    private Boolean calculateBaseURL = false;
    @Column
    private List<String> customHeaders;
    @Column
    private Boolean isJavascriptCrawl = false;
    @Column
    private String staticIPCrawlType = StaticIPCrawlType.NOT.name();
    @Column
    private Boolean noCanonicals = false;
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
        @JsonProperty("staticIPCrawlType") final String staticIPCrawlType,
        @JsonProperty("noCanonicals") final Boolean noCanonicals,
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

        if (null != isJavascriptCrawl)
            this.isJavascriptCrawl = isJavascriptCrawl;

        if (null != noCanonicals)
            this.noCanonicals = noCanonicals;

        if (null != noCookies)
            this.noCookies = noCookies;

        if (null != proxyType && isValidEnum(ProxyType.class, proxyType)) {
            this.proxyType = proxyType;
        }

        if (null != readTimeout)
            this.readTimeout = readTimeout;

        if (null != staticIPCrawlType)
            this.staticIPCrawlType = staticIPCrawlType;

        if (null != userAgent && isValidEnum(UserAgentType.class, userAgent)) {
            this.userAgent = userAgent;
        }

        if (null != notes)
            this.notes = notes;

        this.uuid = UUID.randomUUID();
    }

    @JsonProperty
    public Boolean getJavascriptCrawl() {
        return isJavascriptCrawl;
    }

    @JsonProperty
    public String getRuleScope() {
        return ruleScope;
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
    public Boolean getNoCanonicals() {
        return noCanonicals;
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
    @JsonProperty
    public String getStaticIPCrawlType() {
        return staticIPCrawlType;
    }


    public void setDomain(String domain) {
        this.domain = domain;
    }

    public void setExactMatch(Boolean exactMatch) {
        this.exactMatch = exactMatch;
    }

    public void setCalculateBaseURL(Boolean calculateBaseURL) {
        this.calculateBaseURL = calculateBaseURL;
    }

    public void setCustomHeaders(List<String> customHeaders) {
        this.customHeaders = customHeaders;
    }

    public void setJavascriptCrawl(Boolean javascriptCrawl) {
        isJavascriptCrawl = javascriptCrawl;
    }

    public void setStaticIPCrawlType(String staticIPCrawlType) {
        this.staticIPCrawlType = staticIPCrawlType;
    }

    public void setNoCanonicals(Boolean noCanonicals) {
        this.noCanonicals = noCanonicals;
    }

    public void setNoCookies(Boolean noCookies) {
        this.noCookies = noCookies;
    }

    public void setProxyType(String proxyType) {
        this.proxyType = proxyType;
    }

    public void setReadTimeout(Integer readTimeout) {
        this.readTimeout = readTimeout;
    }

    public void setRuleScope(String ruleScope) {
        this.ruleScope = ruleScope;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public void setNotes(List<String> notes) {
        this.notes = notes;
    }


    public static DomainConfig getEmptyConfig() {
        return new DomainConfig(
            "Fill in a value...",
            false,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            "ALL",
            UserAgentType.SEARCHMETRICSBOT.name()
            );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (! (o instanceof DomainConfig)) return false;

        DomainConfig that = (DomainConfig) o;

        if (! uuid.equals(that.uuid)) return false;
        if (! domain.equals(that.domain)) return false;
        if (! exactMatch.equals(that.exactMatch)) return false;
        if (! calculateBaseURL.equals(that.calculateBaseURL)) return false;
        if (customHeaders != null ? ! customHeaders.equals(that.customHeaders) : that.customHeaders != null)
            return false;
        if (! isJavascriptCrawl.equals(that.isJavascriptCrawl)) return false;
        if (! staticIPCrawlType.equals(that.staticIPCrawlType)) return false;
        if (! noCanonicals.equals(that.noCanonicals)) return false;
        if (! noCookies.equals(that.noCookies)) return false;
        if (! proxyType.equals(that.proxyType)) return false;
        if (! readTimeout.equals(that.readTimeout)) return false;
        if (! ruleScope.equals(that.ruleScope)) return false;
        if (! userAgent.equals(that.userAgent)) return false;
        return notes != null ? notes.equals(that.notes) : that.notes == null;
    }

    @Override
    public int hashCode() {
        int result = uuid.hashCode();
        result = 31 * result + domain.hashCode();
        result = 31 * result + exactMatch.hashCode();
        result = 31 * result + calculateBaseURL.hashCode();
        result = 31 * result + (customHeaders != null ? customHeaders.hashCode() : 0);
        result = 31 * result + isJavascriptCrawl.hashCode();
        result = 31 * result + staticIPCrawlType.hashCode();
        result = 31 * result + noCanonicals.hashCode();
        result = 31 * result + noCookies.hashCode();
        result = 31 * result + proxyType.hashCode();
        result = 31 * result + readTimeout.hashCode();
        result = 31 * result + ruleScope.hashCode();
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
            ", isJavascriptCrawl=" + isJavascriptCrawl +
            ", staticIPCrawlType='" + staticIPCrawlType + '\'' +
            ", noCanonicals=" + noCanonicals +
            ", noCookies=" + noCookies +
            ", proxyType='" + proxyType + '\'' +
            ", readTimeout=" + readTimeout +
            ", ruleScope='" + ruleScope + '\'' +
            ", userAgent='" + userAgent + '\'' +
            ", notes=" + notes +
            '}';
    }
}