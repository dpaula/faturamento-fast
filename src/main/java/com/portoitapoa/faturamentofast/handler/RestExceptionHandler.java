package com.portoitapoa.faturamentofast.handler;


import com.portoitapoa.faturamentofast.error.ResourceNotFoundException;
import com.portoitapoa.faturamentofast.error.WSFaturamentoFastException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

/**
 * @author Fernando de Lima
 */
@ControllerAdvice
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RestExceptionHandler {

    public static final String ERRO_INTERNO_NO_SERVIDOR = "Erro interno no servidor!";
    public static final String INFORMACOES_INCONSISTENTES = "Informações inconsistentes!";
    public static final String CONFLITO_NAS_INFORMACOES = "Não foi possível salvar as informações!";
    public static final String INFORMACOES_NAO_ENCONTRADAS = "Não encontrada informações solicitadas!";
    public static final String SEM_PERMISSAO = "Seu usuário não tem permissão para executar está ação";

    private final MessageSource messageSource;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(final HttpServletRequest req, final Exception exception) {

        if (exception instanceof HttpMessageNotReadableException) {

            final HttpMessageNotReadableException excep = (HttpMessageNotReadableException) exception;
            final var details = buildErrorDetails(excep, req, INFORMACOES_INCONSISTENTES, HttpStatus.BAD_REQUEST,
                excep.getMessage());
            return new ResponseEntity<>(details, HttpStatus.BAD_REQUEST);
        }

        final var details = buildErrorDetails(exception, req, ERRO_INTERNO_NO_SERVIDOR,
            HttpStatus.INTERNAL_SERVER_ERROR,
            exception.getMessage());

        return new ResponseEntity<>(details, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(WSFaturamentoFastException.class)
    public ResponseEntity<?> handleWSInspecaoException(final HttpServletRequest req,
        final WSFaturamentoFastException exception) {

        final var details = Objects.isNull(exception.getTagException())
            ? buildErrorDetails(exception, req, ERRO_INTERNO_NO_SERVIDOR,
            HttpStatus.BAD_REQUEST,
            exception.getMessage())
            : buildErrorDetailsWithMessagei18n(exception, req,
            buildMessageWithPrefix(req, exception.getTagException()
                .getDescricao()),
            exception.getTagException()
                .getI18n(), HttpStatus.BAD_REQUEST,
            exception.getMessage());
        return new ResponseEntity<>(details, HttpStatus.BAD_REQUEST);

    }

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(final HttpServletRequest req,
        final MethodArgumentNotValidException exception) {

        var mensagem = exception.getMessage();

        final var fieldErrors = exception.getBindingResult()
            .getFieldErrors();
        if (!fieldErrors.isEmpty()) {
            final var e = fieldErrors.get(0);
            mensagem = e.getField() + " " + messageSource.getMessage(e, LocaleContextHolder.getLocale());
        }
        final var details = buildErrorDetails(exception, req, INFORMACOES_INCONSISTENTES, HttpStatus.BAD_REQUEST,
            mensagem);
        return new ResponseEntity<>(details, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleObjectNotFoundException(final HttpServletRequest req,
        final ResourceNotFoundException exception) {
        final var details = buildErrorDetails(exception, req, INFORMACOES_NAO_ENCONTRADAS, HttpStatus.NOT_FOUND,
            exception.getMessage());
        return new ResponseEntity<>(details, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDeniedException(final HttpServletRequest req,
                                                         final AccessDeniedException exception) {
        final var details = buildErrorDetails(exception, req, SEM_PERMISSAO, HttpStatus.BAD_REQUEST,
                exception.getMessage());
        return new ResponseEntity<>(details, HttpStatus.BAD_REQUEST);
    }

    private ErrorDetails buildErrorDetails(final Exception exception, final HttpServletRequest req,
        final String message,
        final HttpStatus httpStatus, final String detailveloperMessage) {
        return ErrorDetails.builder()
            .message(detailveloperMessage)
            .messagei18n(null)
            .status(httpStatus.value())
            .timestamp(new Date().getTime())
            .url(req.getRequestURI())
            .detailDeveloper(DetailDeveloperMessage.builder()
                .exceptionClass(exception.getClass()
                    .getName())
                .error(detailveloperMessage)
                .stackTrace(Arrays.toString(exception.getStackTrace()))
                .build())
            .build();
    }

    private ErrorDetails buildErrorDetailsWithMessagei18n(final Exception exception, final HttpServletRequest req,
        final String message, final String messagei18n, final HttpStatus httpStatus,
        final String detailveloperMessage) {
        final ErrorDetails errorDetails = buildErrorDetails(exception, req, message, httpStatus, detailveloperMessage);
        errorDetails.setMessagei18n(messagei18n);
        return errorDetails;
    }

    private String getPrefixMessage(final HttpServletRequest req) {
        final String method = req.getMethod();
        switch (method) {
            case "DELETE":
                return "Não foi possível remover o registro. ";
            case "POST":
                return "Não foi possível registrar o registro. ";
            case "PUT":
                return "Não foi possível atualizar o registro. ";
            default:
                return "";
        }
    }

    private String buildMessageWithPrefix(final HttpServletRequest req, final String message) {
        return getPrefixMessage(req) + message;
    }
}
