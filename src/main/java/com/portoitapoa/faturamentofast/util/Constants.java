package com.portoitapoa.faturamentofast.util;

import java.util.List;

/**
 * @author Alan Lopes
 */
public class Constants {

	private Constants() {
		throw new IllegalStateException("Utility class");
	}

	public static final List<String> EVENTS_CROSSDOCKING = List.of("CROSSDOCKING_MECANIZADO", "CROSSDOCKING_MANUAL", "CROSSDOCKING MECANIZADO", "CROSSDOCKING MANUAL");

}
