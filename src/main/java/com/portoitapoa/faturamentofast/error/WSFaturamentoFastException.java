package com.portoitapoa.faturamentofast.error;

import com.portoitapoa.faturamentofast.enuns.EnTagException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Alan Lopes
 */
@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class WSFaturamentoFastException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private EnTagException tagException;

    public WSFaturamentoFastException(final String mensagem) {
        super(mensagem);
    }

    public WSFaturamentoFastException(final String mensagem, final Throwable origem) {
        super(mensagem, origem);
    }

    public WSFaturamentoFastException(final EnTagException tagException) {
        super(tagException.getDescricao());
        this.tagException = tagException;
    }

    public EnTagException getTagException () {
        return this.tagException;
    }
}
