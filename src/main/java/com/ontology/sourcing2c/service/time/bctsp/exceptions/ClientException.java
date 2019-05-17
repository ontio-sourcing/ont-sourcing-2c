package com.ontology.sourcing2c.service.time.bctsp.exceptions;

@SuppressWarnings("serial")
public class ClientException extends RuntimeException {

    public ClientException(final String p_message) {
        super(p_message);
    }

    public ClientException(final String p_message, final Throwable p_throwable) {
        super(p_message, p_throwable);
    }

}
