package com.portoitapoa.faturamentofast.security.model;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Fernando de Lima on 14/07/20
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@ToString
public class UsuarioContext implements UserDetails {

    private static final long serialVersionUID = -7419645841408061402L;
    private String nomeCompleto;
    private String login;
    private String email;
    private String roles;
    private String cpf;
    private boolean adm;
    @Builder.Default
    private List<String> cnpjClientesVisiveis = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRolesMontados();
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    private List<Perfil> getRolesMontados() {

        final ArrayList<Perfil> rolesMontados = new ArrayList<>();

        if (getRoles() == null) {
            rolesMontados.add(new Perfil("ROLE_USER"));
            return rolesMontados;
        }

        for (final String role : getRoles().split(",")) {
            rolesMontados.add(new Perfil("ROLE_" + role.trim().toUpperCase()));
        }

        return rolesMontados;
    }
}
