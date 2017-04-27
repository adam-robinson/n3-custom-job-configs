package com.searchmetrics.audit.job.config.api.rest;

import com.searchmetrics.audit.job.config.dao.DomainConfigRepository;
import com.searchmetrics.audit.job.config.dto.DomainConfig;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;
import static javax.ws.rs.core.Response.Status;

/**
 * REST-API Endpoint of our audit-job-config.
 *
 * @since 2.0
 */
@Api(value = "AuditJobConfig Resource", description = "Resource containing all of the application's endpoints")
@Path("/")
public class AuditJobConfigEndpoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuditJobConfigEndpoint.class);
    private final DomainConfigRepository domainConfigRepository;

    /**
     * Endpoint
     *
     * @param domainConfigRepository
     */
    public AuditJobConfigEndpoint(DomainConfigRepository domainConfigRepository) {
        this.domainConfigRepository = checkNotNull(domainConfigRepository, "u r not d man!");
    }

    private static ResponseBuilder defaults(final ResponseBuilder builder) {
        return builder.header("X-Powered-By", "Content team").type(MediaType.APPLICATION_JSON_TYPE);
    }

    private static ResponseBuilder ok() {
        return defaults(Response.ok());
    }

    /**
     * This method creates and stores a new {@link DomainConfig} object in the
     * service's DB.  If a config already exists for that domain a 400
     *
     * @param domainConfig a JSON serialized version of a {@link DomainConfig}
     *                     object where only the "domain" field is required.
     * @return
     */
    @POST
    @Path("addConfig")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addConfig(final DomainConfig domainConfig) {
        if (LOGGER.isDebugEnabled())
            LOGGER.debug(String.valueOf(domainConfig));

        DomainConfig dc = domainConfigRepository.findByUuid(domainConfig.getUuid());
        if (null != dc) {
            return Response.status(Status.CONFLICT).entity(dc).build();
        }

        domainConfigRepository.save(domainConfig);

        URI newDomainLocation = UriBuilder.fromResource(AuditJobConfigEndpoint.class)
            .path("/getDomainConfig")
            .queryParam("domain", domainConfig.getDomain()).build();

        return Response.created(newDomainLocation).entity(domainConfig).build();
    }

    /**
     * @param domainParameter String used to match against a value in the
     *                        domain field of the database
     * @return
     */
    @GET
    @Path("listConfigs")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDomainConfigs(@QueryParam("domain") final String domainParameter) {

        Optional<String> domainStringOption = Optional.ofNullable(domainParameter);
        List<DomainConfig> domainConfigList = new ArrayList<>();

        if (domainStringOption.isPresent()) {
            DomainConfig domainConfig = domainConfigRepository.findByDomain(domainParameter);
            if (null != domainConfig) {
                domainConfigList.add(domainConfig);
            }
        } else {
            domainConfigRepository.findAll().forEach(
                dc -> {
                    if (null != dc) {
                        domainConfigList.add(dc);
                    }
                }
            );
        }

        if (domainConfigList.size() == 0) {
            return Response.status(Status.NOT_FOUND)
                .entity(String.format(
                    "No Domain config found with the domain: %s",
                    domainParameter
                    )
                ).build();
        } else {
            return ok().entity(domainConfigList).build();
        }
    }

    /**
     * @param domainConfigList List of {@link DomainConfig} objects to
     *                         update and be saved
     * @return 200 status and the updated configs
     */
    @PUT
    @Path("updateConfigs")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateConfig(@RequestBody final List<DomainConfig> domainConfigList) {
        domainConfigRepository.save(domainConfigList);
        return ok().entity(domainConfigList).build();
    }

    /**
     * Deletes the {@link DomainConfig} equal to the PathParameter in the
     * request URL /deleteConfig/{domain}.
     *
     * @param domainPattern The domain of the config to be deleted
     * @return 200 and the deleted {@link DomainConfig} in
     * its body or 404 if there is no matching domain
     */
    @DELETE
    @Path("deleteConfig/{domain}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteConfig(@PathParam("domain") final String domainPattern) {
        Optional<DomainConfig> potentialDomainConfig = Optional.of(domainConfigRepository.findByDomain(domainPattern));
        if (! potentialDomainConfig.isPresent()) {
            return Response.status(Status.NOT_FOUND)
                .entity(MessageFormat.format("No DomainConfig matching '{0}' was found!", domainPattern))
                .build();
        } else {
            DomainConfig toBeDeletedConfig = potentialDomainConfig.get();
            domainConfigRepository.delete(toBeDeletedConfig);
            return ok().entity(toBeDeletedConfig).build();
        }
    }

    /**
     * @return Response
     */
    @GET
    @Path("status")
    public Response status() {
        return ok().entity("OK").type(MediaType.TEXT_PLAIN_TYPE).build();
    }

}
