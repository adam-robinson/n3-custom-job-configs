package com.searchmetrics.audit.job.config.api.protocol;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.base.Strings;
import com.google.common.base.Throwables;

import java.util.Optional;

/**
 * A POJO that represents an error response from word-to-vector service
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuditJobConfigErrorResponse {

    private final String error;
    private final String clazz;

    /**
     * Build an error response from a throwable
     *
     * @param e              Throwable causing the error
     * @param dumpStackTrace dumpStackTrace
     */
    public AuditJobConfigErrorResponse(final Throwable e, final Optional<Boolean> dumpStackTrace) {

        if(dumpStackTrace.isPresent() && dumpStackTrace.get()) {
            this.error = Throwables.getStackTraceAsString(e);
        } else {
            if(Strings.isNullOrEmpty(e.getMessage())) {
                this.error = "something went wrong";
            } else {
                this.error = e.getMessage();
            }
        }
        this.clazz = e.getClass().getCanonicalName();
    }

    public String getError() {
        return error;
    }

    public String getExceptionClass() {
        return clazz;
    }

}
