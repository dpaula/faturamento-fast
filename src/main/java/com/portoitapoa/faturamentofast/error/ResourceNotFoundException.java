package com.portoitapoa.faturamentofast.error;

import com.portoitapoa.faturamentofast.enuns.EnTagException;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.NOT_FOUND;

/**
 * @author Fernando de Lima
 */
@ResponseStatus(NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 190564801840132755L;

    public ResourceNotFoundException(final String message) {
        super(message);
    }

    public ResourceNotFoundException(final EnTagException faturamentoFastException) {
        super(faturamentoFastException.name());
    }
}
