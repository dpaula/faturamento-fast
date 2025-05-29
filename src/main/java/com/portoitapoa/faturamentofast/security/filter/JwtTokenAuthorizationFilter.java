package com.portoitapoa.faturamentofast.security.filter;

import com.portoitapoa.faturamentofast.security.converter.TokenConverter;
import com.portoitapoa.faturamentofast.security.converter.prop.JwtConfiguration;
import com.portoitapoa.faturamentofast.security.util.SecurityContextUtil;
import com.portoitapoa.faturamentofast.util.Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Fernando de Lima
 */
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class JwtTokenAuthorizationFilter extends OncePerRequestFilter {

    protected final JwtConfiguration jwtConfiguration;
    protected final TokenConverter tokenConverter;

    @Override
    protected void doFilterInternal(@NonNull final HttpServletRequest request,
                                    @NonNull final HttpServletResponse response,
                                    @NonNull final FilterChain filterChain) throws ServletException, IOException {

        final var token = recuperarToken(request);
        requestLogging(request, token);
        filterChain.doFilter(request, response);
    }

    private void requestLogging(HttpServletRequest request, String token) {
        if (StringUtils.isNotBlank(token)) {
            final boolean valido = tokenConverter.isTokenValido(token);
            if (valido) {
                autenticarCliente(token);
                log.info("{} Requisição {} {} autorizada com sucesso!",
                        Util.LOG_PREFIX, request.getMethod(), request.getRequestURI());
            } else {
                log.warn("{} Requisição {} {} recebeu token inválido!",
                        Util.LOG_PREFIX, request.getMethod(), request.getRequestURI());
            }
        }
    }

    private void autenticarCliente(final String token) {

        final var claims = tokenConverter.getClaimsToken(token);

        SecurityContextUtil.setSecurityContext(claims, token);
    }

    private String recuperarToken(final HttpServletRequest request) {
        final String token = request.getHeader(jwtConfiguration.getHeader().getName());

        if (StringUtils.isBlank(token) || !token.startsWith(jwtConfiguration.getHeader().getPrefix())) {
            return null;
        }

        return token.replace(jwtConfiguration.getHeader().getPrefix(), "").trim();
    }
}