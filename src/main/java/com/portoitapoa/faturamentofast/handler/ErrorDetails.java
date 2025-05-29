package com.portoitapoa.faturamentofast.handler;

import lombok.*;

/**
 * @author Alan Lopes
 */
@Builder
@Getter
@ToString
public class ErrorDetails {

    private final String message;
    @Setter(AccessLevel.PUBLIC)
    private String messagei18n;
    private final int status;
    private final long timestamp;
    private final String url;
    private final DetailDeveloperMessage detailDeveloper;
}
