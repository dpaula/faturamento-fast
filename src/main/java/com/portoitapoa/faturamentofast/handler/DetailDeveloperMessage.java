package com.portoitapoa.faturamentofast.handler;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * @author Alan Lopes
 */
@Builder
@Getter
@ToString
public class DetailDeveloperMessage {
    private final String exceptionClass;
    private final String error;
    private final String stackTrace;
}
