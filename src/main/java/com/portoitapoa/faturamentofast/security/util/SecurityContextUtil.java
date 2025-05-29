package com.portoitapoa.faturamentofast.security.util;

import com.portoitapoa.faturamentofast.security.model.UsuarioContext;
import com.portoitapoa.faturamentofast.util.Util;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Fernando de Lima
 */
@Slf4j
public class SecurityContextUtil {
    private SecurityContextUtil() {
    }

    @SuppressWarnings("unchecked")
    public static void setSecurityContext(@NotNull final Claims claims, final String token) {
        log.info("{} Definindo contexto de segurança do usuário [{}]", Util.LOG_PREFIX, claims.getSubject());
        try {

            final List<String> authorities = (List<String>) claims.get("authorities");
            final List<String> cnpjClientesVisiveis = getListaCompleta((String) claims.get("cnpjClientesVisiveis"));
            final String email = claims.get("email", String.class);
            final String cpf = claims.get("cpf", String.class);
            final String nomeCompleto = claims.get("nomeCompleto", String.class);

            final var usuarioContext = UsuarioContext.builder()
                .login(claims.getSubject())
                .roles(String.join(",", authorities))
                .cnpjClientesVisiveis(cnpjClientesVisiveis)
                .email(email)
                .cpf(cpf)
                .nomeCompleto(nomeCompleto)
                .build();

            final UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                usuarioContext,
                null, usuarioContext.getAuthorities());

            authentication.setDetails(token);

            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (final Exception e) {
            log.error("{} Erro ao definir contexto de segurança", Util.LOG_PREFIX, e);
            SecurityContextHolder.clearContext();
        }
    }

    private static List<String> getListaCompleta(final String cnpjClientesVisiveisString) {

        List<String> cnpjClientesVisiveis = Collections.emptyList();

        if (StringUtils.isNotBlank(cnpjClientesVisiveisString)) {
            try {
                final List<String> radicaisGrupo = List.of(cnpjClientesVisiveisString.split(";"));

                cnpjClientesVisiveis = new ArrayList<>();

                for (final String radicalGrupoString : radicaisGrupo) {

                    final String[] radicalGrupo = radicalGrupoString.split("-");
                    final String radical = radicalGrupo[0];
                    final List<String> grupo = List.of(radicalGrupo[1].split(","));

                    cnpjClientesVisiveis.addAll(grupo.stream()
                        .map(radical::concat)
                        .collect(Collectors.toList()));
                }
            } catch (final Exception e) {
                log.error("{} Erro ao converter CNPJs retornados das claims", Util.LOG_PREFIX, e);
                return List.of();
            }
        }
        return cnpjClientesVisiveis;
    }
}