package com.portoitapoa.faturamentofast.util;

import com.portoitapoa.faturamentofast.error.WSFaturamentoFastException;
import com.portoitapoa.faturamentofast.security.model.UsuarioContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

/**
 * @author Fernando de Lima
 */
@Component
@Service
@Configuration
public class Util {

	public static String LOG_PREFIX;

	public static final ZoneId ZONA_ID = ZoneId.of("America/Sao_Paulo");

	private static final ThreadLocal<String> CORRELATION_ID = new ThreadLocal<>();

	public static String getRadicalCnpj(final String cnpjExportador) {

		if (StringUtils.isBlank(cnpjExportador) || cnpjExportador.length() < 8) {
			throw new WSFaturamentoFastException("CNPJ inválido para extrair o radical!");
		}

		return cnpjExportador.trim().substring(0, 8);
	}

	public static UsuarioContext getUsuarioCorrente() {
		return (UsuarioContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}

	public static Authentication getAuthentication() {
		return (Authentication) SecurityContextHolder.getContext().getAuthentication();
	}

	@Value("${server.undertow.accesslog.prefix}")
	public void setLogPrefix(final String logPrefix) {
		Util.LOG_PREFIX = logPrefix;
	}

	public static boolean containAnyInList(final List<String> list, final List<String> subList) {
		for(final String item : subList) {
			if(list.contains(item)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Generates a new correlation ID (UUID) and stores it in the ThreadLocal variable.
	 *
	 * @return the generated correlation ID
	 */
	public static String generateCorrelationId() {
		String cid = UUID.randomUUID().toString();
		CORRELATION_ID.set(cid);
		return cid;
	}

	/**
	 * Sets the correlation ID in the ThreadLocal variable.
	 *
	 * @param correlationId the correlation ID to set
	 */
	public static void setCorrelationId(String correlationId) {
		CORRELATION_ID.set(correlationId);
	}

	/**
	 * Gets the correlation ID from the ThreadLocal variable.
	 * If no correlation ID is set, generates a new one.
	 *
	 * @return the correlation ID
	 */
	public static String getCorrelationId() {
		String cid = CORRELATION_ID.get();
		if (cid == null) {
			cid = generateCorrelationId();
		}
		return cid;
	}

	/**
	 * Clears the correlation ID from the ThreadLocal variable.
	 */
	public static void clearCorrelationId() {
		CORRELATION_ID.remove();
	}
}
