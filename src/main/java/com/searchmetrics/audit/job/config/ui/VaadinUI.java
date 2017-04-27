package com.searchmetrics.audit.job.config.ui;

import com.searchmetrics.audit.job.config.dao.DomainConfigRepository;
import com.searchmetrics.audit.job.config.dto.DomainConfig;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;

/**
 *
 */
@SpringUI
public class VaadinUI extends UI {
    private final DomainConfigRepository domainConfigRepository;
    private final DomainConfigEditor domainConfigEditor;

    final Grid<DomainConfig> domainConfigGrid;

    final TextField filter;

    private final Button addNewButton;

    @Autowired
    public VaadinUI(DomainConfigRepository domainConfigRepository, DomainConfigEditor domainConfigEditor) {
        this.domainConfigRepository = domainConfigRepository;
        this.domainConfigEditor = domainConfigEditor;
        this.domainConfigGrid = new Grid<>(DomainConfig.class);
        this.filter = new TextField();
        this.addNewButton = new Button("New DomainConfig");
    }


    @Override
    protected void init(VaadinRequest request) {
        HorizontalLayout actions = new HorizontalLayout(filter, addNewButton);
        VerticalLayout main = new VerticalLayout(actions, domainConfigGrid, domainConfigEditor);
        setContent(main);

        domainConfigGrid.setWidth(1280, Unit.PIXELS);
        domainConfigGrid.setHeight(360, Unit.PIXELS);
        domainConfigGrid.setColumns("uuid", "domain", "exactMatch",
            "calculateBaseURL", "customHeaders", "javascriptCrawl", "noCanonicals", "noCookies",
            "proxyType", "readTimeout", "staticIPCrawlType", "userAgent", "notes");

        filter.setPlaceholder("Filter By Domain");

        filter.setValueChangeMode(ValueChangeMode.LAZY);
        filter.addValueChangeListener(e -> listDomainConfigs(e.getValue()));

        // Connect selected Customer to editor or hide if none is selected
        domainConfigGrid.asSingleSelect().addValueChangeListener(e -> {
            domainConfigEditor.editDomainConfig(e.getValue());
        });

        // Instantiate and edit new Customer the new button is clicked
        addNewButton.addClickListener(e -> domainConfigEditor.editDomainConfig(DomainConfig.getEmptyConfig()));

        // Listen changes made by the editor, refresh data from backend
        domainConfigEditor.setChangeHandler(() -> {
            domainConfigEditor.setVisible(false);
            listDomainConfigs(filter.getValue());
        });

        // Initialize listing
        listDomainConfigs(null);
    }

    void listDomainConfigs(String filterText) {
        Collection<DomainConfig> allConfigs = (Collection<DomainConfig>) domainConfigRepository.findAll();
        if (StringUtils.isEmpty(filterText)) {
            domainConfigGrid.setItems(allConfigs);
        }
        else {
            domainConfigGrid.setItems(allConfigs.stream()
                .filter(dc -> dc.getDomain().toLowerCase().indexOf(
                    filterText.toLowerCase()) >= 0)
            );
        }
    }
}
