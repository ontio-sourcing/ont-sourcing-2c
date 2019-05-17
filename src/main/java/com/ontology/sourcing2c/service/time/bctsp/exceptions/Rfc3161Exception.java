package com.ontology.sourcing2c.service.time.bctsp.exceptions;

@SuppressWarnings("serial")
public class Rfc3161Exception extends RuntimeException {

    public Rfc3161Exception(final String p_message, final Throwable p_throwable) {
        super(p_message, p_throwable);
    }

    public Rfc3161Exception(final String p_message) {
        super(p_message);
    }

}
