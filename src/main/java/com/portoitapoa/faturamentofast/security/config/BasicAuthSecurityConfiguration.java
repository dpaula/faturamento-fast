package com.portoitapoa.faturamentofast.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.cors.CorsConfiguration;

/**
 * 
 * @author Alisson Lopes
 *
 */
@Profile("basic-auth")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Configuration
@EnableWebSecurity
@Order(2)
public class BasicAuthSecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		final CorsConfiguration corsConfiguration = new CorsConfiguration().applyPermitDefaultValues();
		corsConfiguration.addAllowedMethod(HttpMethod.DELETE);
		corsConfiguration.addAllowedMethod(HttpMethod.PUT);

		http.cors().configurationSource(request -> corsConfiguration).and()
			.authorizeRequests()
				.antMatchers("**/swagger-resources/**",
					"/**/swagger-ui.html",
					"/**/swagger-ui/**",
					"/swagger-resources",
					"/**/swagger-resources/**",
					"/v2/api-docs/**",
					"/swagger-config/**",
					"/**/webjars/springfox-swagger-ui/**").authenticated()
				.and().formLogin();

	}

	@Override
	public void configure(final WebSecurity web) {
		web.ignoring().antMatchers("/**.html", "/webjars/**", "/configuration/**",
				"/swagger-resources/**", "/h2-console/**");
	}

}
