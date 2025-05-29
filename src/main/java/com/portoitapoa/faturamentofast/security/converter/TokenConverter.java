package com.portoitapoa.faturamentofast.security.converter;

import com.portoitapoa.faturamentofast.security.converter.prop.JwtConfiguration;
import com.portoitapoa.faturamentofast.util.Util;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotBlank;

/**
 * @author Fernando de Lima
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TokenConverter {

    private final JwtConfiguration jwtConfiguration;

    public boolean isTokenValido(final String token) {
        log.info("{} Validando assinatura do token", Util.LOG_PREFIX);
        try {
            Jwts.parser().setSigningKey(jwtConfiguration.getPrivateKey()).parseClaimsJws(token);
            log.info("{} Token válido", Util.LOG_PREFIX);
            return true;
        } catch (final Exception e) {
            log.error("{} Token inválido!", Util.LOG_PREFIX, e);
            return false;
        }
    }

    public Claims getClaimsToken(@NotBlank final String token) {
        log.info("{} Desencriptografando as claims do token", Util.LOG_PREFIX);
        return Jwts.parser()
            .setSigningKey(jwtConfiguration.getPrivateKey())
            .parseClaimsJws(token)
            .getBody();
    }
}