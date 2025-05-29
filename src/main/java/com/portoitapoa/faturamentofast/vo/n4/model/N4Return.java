package com.portoitapoa.faturamentofast.vo.n4.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

/**
 * @author Carlos Lima on 15/06/2021
 */

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class N4Return {

	@JsonProperty("className")
	private String className;

	@JsonProperty("classLocation")
	private String classLocation;

	@JsonProperty("status")
	private String status;

	@JsonProperty("message")
	private String message;
}
