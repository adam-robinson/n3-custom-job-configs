package com.searchmetrics.audit.job.config.api.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.searchmetrics.audit.job.config.api.protocol.AuditJobConfigErrorResponse;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.Optional;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;

/**
 * Providers for exception mappers (exception -> response)
 */
public class ExceptionMappers {
    private static final ObjectMapper OM = new ObjectMapper();
    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionMappers.class);
    private static final Boolean DUMP_STACK_IN_RESPONSE = true;

    private ExceptionMappers() {
    }

    private static Response createResponse(final Response.Status status, final Throwable e) {

        if(e instanceof WebApplicationException) {
            return ((WebApplicationException) e).getResponse();
        }

        LOGGER.error("Creating Error Response due to uncaught exception", e);

        try {
            return Response.status(status)
                    .entity(OM.writeValueAsString(new AuditJobConfigErrorResponse(e, Optional.of(DUMP_STACK_IN_RESPONSE))))
                    .build();

        } catch(JsonProcessingException e1) {
            LOGGER.error("Error while serializing AuditJobConfigErrorResponse", e1);
            final JSONObject o = new JSONObject();
            o.put("error", "cannot do anything");
            return Response.status(INTERNAL_SERVER_ERROR).entity(o.toJSONString()).build();
        }
    }

    /**
     * Provider for ExceptionMapper for IllegalArgumentException
     */
    @Provider
    public static class IllegalArgumentExceptionMapper implements ExceptionMapper<IllegalArgumentException> {

        @Override
        public Response toResponse(final IllegalArgumentException e) {
            return createResponse(BAD_REQUEST, e);
        }
    }

    /**
     * Provider for ExceptionMapper for Throwable
     */
    @Provider
    public static class GenericExceptionMapper implements ExceptionMapper<Throwable> {

        @Override
        public Response toResponse(final Throwable e) {
            return createResponse(INTERNAL_SERVER_ERROR, e);
        }
    }
}
