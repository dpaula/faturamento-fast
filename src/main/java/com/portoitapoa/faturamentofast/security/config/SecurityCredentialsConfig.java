package com.portoitapoa.faturamentofast.security.config;

import com.portoitapoa.faturamentofast.security.converter.TokenConverter;
import com.portoitapoa.faturamentofast.security.converter.prop.JwtConfiguration;
import com.portoitapoa.faturamentofast.security.filter.JwtTokenAuthorizationFilter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author Fernando de Lima
 */
@EnableWebSecurity
public class SecurityCredentialsConfig extends SecurityTokenConfig {
    private final TokenConverter tokenConverter;

    public SecurityCredentialsConfig(final JwtConfiguration jwtConfiguration, final TokenConverter tokenConverter) {
        super(jwtConfiguration);
        this.tokenConverter = tokenConverter;
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.addFilterBefore(new JwtTokenAuthorizationFilter(jwtConfiguration, tokenConverter),
            UsernamePasswordAuthenticationFilter.class);
        super.configure(http);
    }
}