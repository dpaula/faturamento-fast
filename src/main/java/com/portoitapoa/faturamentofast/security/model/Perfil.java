package com.portoitapoa.faturamentofast.security.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

/**
 * @author Fernando de Lima
 */
@Getter
@Setter
@AllArgsConstructor
public class Perfil implements GrantedAuthority {

    private static final long serialVersionUID = 1L;

    private String nome;

    @Override
    public String getAuthority() {
        return nome;
    }
}