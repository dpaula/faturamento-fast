package com.portoitapoa.faturamentofast.service;

import com.portoitapoa.faturamentofast.client.impl.FaturamentoN4Client;
import com.portoitapoa.faturamentofast.client.impl.HubN4Client;
import com.portoitapoa.faturamentofast.groovy.BuscaInformacaoBLGroovy;
import com.portoitapoa.faturamentofast.groovy.output.BuscaInformacaoBLWebService;
import com.portoitapoa.faturamentofast.util.Util;
import com.portoitapoa.faturamentofast.vo.InformacoesBLVO;
import com.portoitapoa.faturamentofast.vo.n4.builder.GroovyN4;
import com.portoitapoa.faturamentofast.vo.n4.vo.N4InputVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Alan Lopes
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class N4Service {

	private final HubN4Client hubN4Client;

	public List<InformacoesBLVO> buscarInformacoesBLPorCE(final String ce, final String bl) {
		log.info("{} Chamando serviço de busca de informacoes do BL: {} no n4 pelo CE: {}", Util.LOG_PREFIX, bl, ce);
		return buscarInformacoesBL(null, ce, bl);
	}

	public List<InformacoesBLVO> buscarInformacoesBLPorDI(final String di, final String bl) {
		log.info("{} Chamando serviço de busca de informacoes do BL: {} no n4 pela DI: {}", Util.LOG_PREFIX, bl, di);
		return buscarInformacoesBL(di, null, bl);
	}

	private List<InformacoesBLVO> buscarInformacoesBL(final String di, final String ce, final String bl) {
		try {
			final BuscaInformacaoBLGroovy input = montarGroovyPorSiscomex(di, ce, bl);
			log.info("{} Chamando serviço de busca de informacoes do BL no n4: {}", Util.LOG_PREFIX, input.toString());
			final N4InputVO inputGroovy = GroovyN4.inputExcludeNull(input);
			final String json = hubN4Client.executaGroovyN4(inputGroovy);
			final var result = (BuscaInformacaoBLWebService) GroovyN4.output(
					json,
					BuscaInformacaoBLWebService.class);
			if (Boolean.TRUE.equals(GroovyN4.isSuccess(result)))
				return result.getParameter();

			return new ArrayList<>();
		} catch (final Exception ex) {
			log.error("{} Houve um erro ao buscar as informacoes do BL {} no N4!", Util.LOG_PREFIX, bl);
			return new ArrayList<>();
		}
	}


	private BuscaInformacaoBLGroovy montarGroovyPorSiscomex(final String di, final String ce, final String bl){
		if(ce != null)
			return BuscaInformacaoBLGroovy.builder()
					.bl(bl)
					.ce(ce)
					.build();

		return BuscaInformacaoBLGroovy.builder()
				.bl(bl)
				.internalDeliveryNumberValue(di)
				.build();
	}



}
