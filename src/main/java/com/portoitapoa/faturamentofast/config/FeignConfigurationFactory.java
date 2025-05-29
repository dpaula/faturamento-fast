package com.portoitapoa.faturamentofast.config;

import org.springframework.cloud.openfeign.support.PageJacksonModule;
import org.springframework.cloud.openfeign.support.SortJacksonModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Dionízio Inácio on 10/05/2024
 */
@Configuration
public class FeignConfigurationFactory {

	@Bean
	public PageJacksonModule pageJacksonModule() {
		return new PageJacksonModule();
	}

	@Bean
	public SortJacksonModule sortJacksonModule() {
		return new SortJacksonModule();
	}
}
