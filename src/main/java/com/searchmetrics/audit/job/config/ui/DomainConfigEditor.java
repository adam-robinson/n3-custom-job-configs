package com.searchmetrics.audit.job.config.ui;

import com.searchmetrics.audit.job.config.dao.DomainConfigRepository;
import com.searchmetrics.audit.job.config.dto.DomainConfig;
import com.vaadin.data.Binder;
import com.vaadin.data.ValueProvider;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Setter;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.searchmetrics.audit.job.config.dto.DomainConfig.ProxyType;
import static com.searchmetrics.audit.job.config.dto.DomainConfig.RuleScope;
import static com.searchmetrics.audit.job.config.dto.DomainConfig.StaticIPCrawlType;
import static com.searchmetrics.audit.job.config.dto.DomainConfig.UserAgentType;

/**
 *
 */
@SpringComponent
@UIScope
public class DomainConfigEditor extends VerticalLayout {
    private static final Collection<String> PROXY_TYPES =
        Arrays.stream(ProxyType.values()).map(pt -> pt.name())
            .collect(Collectors.toList());
    private static final Collection<String> RULE_SCOPE =
        Arrays.stream(RuleScope.values()).map(pt -> pt.name())
            .collect(Collectors.toList());
    private static final Collection<String> STATIC_IP_CRAWL_TYPE =
        Arrays.stream(StaticIPCrawlType.values()).map(pt -> pt.name())
            .collect(Collectors.toList());
    private static final Collection<String> USER_AGENT_TYPES =
        Arrays.stream(UserAgentType.values()).map(pt -> pt.name())
            .collect(Collectors.toList());

    private final DomainConfigRepository repository;

    private DomainConfig domainConfig;

    Button save = new Button("Save", FontAwesome.SAVE);
    Button cancel = new Button("Cancel");
    Button delete = new Button("Delete", FontAwesome.TRASH_O);
    CssLayout actions = new CssLayout(save, cancel, delete);

    Binder<DomainConfig> domainConfigBinder = new Binder<>(DomainConfig.class);

    TextField uuid = new TextField("UUID");
    TextField domain = new TextField("Domain");
    CheckBox exactMatch =
        new CheckBox("Exact domain string match?");
    CheckBox calculateBaseURL =
        new CheckBox("Recalculate base URL per request?");
    TextArea customHeaders = new TextArea("Custom Headers");
    CheckBox javascriptCrawl = new CheckBox("JavaScript Crawl");
    ListSelect<String> staticIPCrawlType =
        new ListSelect<>("Static IP Crawl", STATIC_IP_CRAWL_TYPE);
    CheckBox noCanonicals = new CheckBox("No Canonicals");
    CheckBox noCookies = new CheckBox("No Cookies");
    ListSelect<String> proxyType = new ListSelect<String>("Proxy Type", PROXY_TYPES);
    TextField readTimeout = new TextField("Read Timeout", "20");
    ListSelect<String> ruleScope = new ListSelect<>("Rule Scope", RULE_SCOPE);
    ListSelect<String> userAgent =
        new ListSelect<>("UserAgent", USER_AGENT_TYPES);
    TextArea notes = new TextArea("Notes");


    @Autowired
    public DomainConfigEditor(DomainConfigRepository repository) {
        this.repository = repository;
        domainConfigBinder.bind(uuid, new ValueProvider<DomainConfig, String>() {
            @Override
            public String apply(DomainConfig domainConfig) {
                return domainConfig.getUuid().toString();
            }
        }, null);
        domainConfigBinder.bind(domain, DomainConfig::getDomain, DomainConfig::setDomain);
        domainConfigBinder.bind(exactMatch, DomainConfig::getExactMatch, DomainConfig::setExactMatch);
        domainConfigBinder.bind(calculateBaseURL, DomainConfig::getCalculateBaseURL, DomainConfig::setCalculateBaseURL);
        domainConfigBinder.bind(javascriptCrawl, DomainConfig::getJavascriptCrawl, DomainConfig::setJavascriptCrawl);
        domainConfigBinder.bind(noCanonicals, DomainConfig::getNoCanonicals, DomainConfig::setNoCanonicals);
        domainConfigBinder.bind(noCookies, DomainConfig::getNoCookies, DomainConfig::setNoCookies);

        domainConfigBinder.bind(notes,
            new ValueProvider<DomainConfig, String>() {
                @Override
                public String apply(DomainConfig domainConfig) {
                    List<String> notesList = domainConfig.getNotes();
                    if (null == notesList || notesList.size() < 1 )
                        return "";

                    return String.join("\n", notesList);
                }
            },
            new Setter<DomainConfig, String>() {
                @Override
                public void accept(DomainConfig domainConfig, String fieldValue) {
                    domainConfig.setNotes(Arrays.asList(fieldValue.split("\n")));
                }
            }
        );

        domainConfigBinder.bind(customHeaders,
            new ValueProvider<DomainConfig, String>() {
                @Override
                public String apply(DomainConfig domainConfig) {
                    List<String> customHeadersList = domainConfig.getCustomHeaders();
                    if (null == customHeadersList || customHeadersList.size() < 1 )
                        return "";

                    return String.join("", customHeadersList);
                }
            },
            new Setter<DomainConfig, String>() {
                @Override
                public void accept(DomainConfig domainConfig, String fieldValue) {
                    domainConfig.setCustomHeaders(Arrays.asList(fieldValue.split("\n")));
                }
            }
        );

        uuid.setReadOnly(true);

        addComponents(
            uuid,
            domain,
            exactMatch,
            calculateBaseURL,
            customHeaders,
            javascriptCrawl,
            staticIPCrawlType,
            noCanonicals,
            noCookies,
            proxyType,
            readTimeout,
            ruleScope,
            userAgent,
            notes,
            actions
        );

        save.addClickListener(e -> repository.save(domainConfig));
        delete.addClickListener(e -> repository.delete(domainConfig));
        cancel.addClickListener(e -> editDomainConfig(domainConfig));
        setVisible(false);
    }

    public interface ChangeHandler {
        void onChange();
    }

    public void editDomainConfig(DomainConfig dc) {
        if (null == dc) {
            setVisible(false);
            return;
        }

        Boolean persisted = false;
        Optional<DomainConfig> optionalDCStored =
            Optional.ofNullable(repository.findByUuid(dc.getUuid()));
        if (optionalDCStored.isPresent()) {
            domainConfig = optionalDCStored.get();
            persisted = true;
        }
        else {
            domainConfig = dc;
        }

        cancel.setVisible(persisted);
        domainConfigBinder.setBean(domainConfig);
        setVisible(true);
        save.focus();
        domain.selectAll();
    }

    public void setChangeHandler(ChangeHandler changeHandler) {
        save.addClickListener(e -> changeHandler.onChange());
        delete.addClickListener(e -> changeHandler.onChange());
    }
}
