package com.portoitapoa.faturamentofast.enuns;

import com.portoitapoa.faturamentofast.error.WSFaturamentoFastException;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Fernando de Lima
 */
@Getter
public enum EnTipoProcessoFaturamento {
	NULO(0, List.of("Nulo"), "FELIPE opa kk"),
	AVISO1(1, List.of("1º aviso"), "Importação"),
	AVISO2(2, List.of("2º aviso"), "Importação"),
	AVISO3(3, List.of("3º aviso"), "Importação"),
	BBK_LCL(4, List.of("Break bulk LCL"), "Importação"),
	CARGA_APREENDIDA(5, List.of("Carga apreendida"), "Importação"),
	CE_BLOQUEADO_SRF(6, List.of("CE bloqueado SRF"), "Importação"),
	CE_SERVICO(7, List.of(" CE de Serviço"), "Importação"),
	CTAC(8, List.of("CTAC"), "Importação"),
	DAT(9, List.of("DAT"), "Exportação"),
	DOCUMENTO_ACOMPANHAMENTO_TRANSPORTE(10, List.of("Documento acompanhamento transporte"), "Exportação"),
	DDE_DSE(11, List.of("DDE/DSE"), "Exportação"),
	DESOVADO_TERMINAL(12, List.of("Desovado Terminal"), "Importação"),
	DI(13, List.of("Declaração de Importação", "DI"), "Importação"),
	DI_ENTREGA_ANTECIPADA(14, List.of("DI - ENTREGA ANTECIPADA", "ENTREGA_ANTECIPADA"), "Importação"),
	DI_LCL(15, List.of("DI - LCL"), "Importação"),
	DI_REGISTRADA(16, List.of("Processo vinculado DI"), "Importação"),
	DSA_DESEMBARACO_SOBRE_AGUAS(17,
		List.of("DSA - DESEMBARACO SOBRE AGUAS", "Desembaraço Sobre Águas - OEA", "DSA - DESEMBARAÇO SOBRE AGUAS"),
		"Importação"),
	DSI(18, List.of("Declaração Simplificada De Importação", "DSI"), "Importação"),
	DTA(19, List.of("Declaração Transito Aduaneiro", "DTA"), "Importação"),
	DTA_COMBOIO(20, List.of("DTA - COMBOIO"), "Importação"),
	DTA_STORAGE(24, List.of("DTA Armazenamento"), "Importação"),
	DTA_LCL(21, List.of("Declaração Transito Aduaneiro - LCL","DTA - LCL"), "Importação"),
	DTA_MASTER(22, List.of("DTA - MASTER"), "Importação"),
	DTA_PATIO(23, List.of("Declaração Transito Aduaneiro bloqueio siscarga"), "Importação"),
	DTA_ARMAZENAMENTO(24, List.of("DTA Armazenamento"), "Importação"),
	DTA_CANCELADA(25, List.of("Declaração Transito Aduaneiro Cancelada"), "Importação"),
	DTA_CONCLUSAO_TRANSITO(26, List.of("DTA CONCLUSAO DE TRANSITO"), "Importação"),
	DTA_DESCARGA_DIRETA(27, List.of("DTA Descarga Direta"), "Importação"),
	DTA_REGISTRADA(28, List.of("Processo Vinculado DTA"), "Importação"),
	DTA_SIMPLIFICADA(29, List.of("DTA-Simplificada"), "Importação"),
	DTC(30, List.of("Declaração de Transito de Container", "DTC NOVO", "DTC"), "Importação"),
	DTT(31, List.of("Declaração de trânsito DE transferência"), "Exportação"),
	DU_IMP(32, List.of("DU-IMP"), "Importação"),
	DUE(33, List.of("Declaração Unica de exportação"), "Exportação"),
	ESTUF_TERMINAL(34, List.of("UNITIZADO NO TERMINAL"), "Exportação"),
	FMA(35, List.of("Ficha Mercadoria Abandonada"), "Importação"),
	HBL_DIVERGENTE(36, List.of("HBL DIVERGENTE"), "Importação"),
	LIBERADO_IOA(37, List.of("LIBERADO IOA"), "Exportação"),
	LIBERADO_ITJ(38, List.of("LIBERADO ITJ"), "Exportação"),
	LIBERADO_SFS(39, List.of("LIBERADO SFS"), "Exportação"),
	MHBL_DIVERGENTE(40, List.of("MHBL DIVERGENTE"), "Importação"),
	MIC_DTA(41, List.of("MIC-DTA"), "Exportação"),
	MSK_DIT(42, List.of("MSK_DIT"), "Exportação"),
	PCI(43, List.of("PCI"), "Exportação"),
	PCI_DAT(44, List.of("PCI - DAT"), "Exportação"),
	PERDIMENTO_TG(45, List.of("Perdimento  Termo de Guarda"), "Importação"),
	PIER(46, List.of("Pier"), "Importação"),
	PROCESSO_ADM(47, List.of("PROC.ADM", "PROCESSO_ADMINISTRATIVO"), "Importação"),
	QUEBRA_LOTE(48, List.of("Quebra de Lote"), "Importação"),
	RETENCAO_RFB(49, List.of("retenção RFB"), "Importação"),
	SEGREGACAO_MADEIRA(50, List.of("Segregração MADEIRA"), "Importação"),
	TRADEX(51, List.of("TRADEX"), "Exportação"),
	TRANSITO_ROVOVIARIO(52, List.of("TRANSITO RODOVIARIO"), "Exportação"),
	TRANSITO_SIMPLIFICADO(53, List.of("TRANSITO SIMPLIFICADO"), "Exportação"),
	TS(54, List.of("Transbordo"), ""),
	AGENDAMENTO(55, List.of("Processos Agendamento Serviços NO SHOW"), "Agendamento");

	private final Integer id;
	private final List<String> desc;
	private final String tipo;

	EnTipoProcessoFaturamento(final Integer id, final List<String> desc, final String tipo) {
		this.desc = desc;
		this.id = id;
		this.tipo = tipo;
	}

	public static EnTipoProcessoFaturamento buscarPorDescricao(final String status) {
		final List<EnTipoProcessoFaturamento> listagemStatus = Arrays.stream(EnTipoProcessoFaturamento.values())
				.filter(statusProgramacao -> statusProgramacao.getDesc().contains(status))
				.collect(Collectors.toList());
		if (!listagemStatus.isEmpty())
			return listagemStatus.get(0);
		return null;
	}

	public static EnTipoProcessoFaturamento valueFromDescTipo(final String tipo) {
		if (StringUtils.isBlank(tipo)) {
			throw new WSFaturamentoFastException("Tipo de Processo Faturamento não informado!");
		}
		if (DI_LCL.getDesc().contains(tipo)) {
			return DI_LCL;
		}
		if (DU_IMP.getDesc().contains(tipo)) {
			return DU_IMP;
		}
		if (DSA_DESEMBARACO_SOBRE_AGUAS.getDesc().contains(tipo)) {
			return DSA_DESEMBARACO_SOBRE_AGUAS;
		}
		if (DI_ENTREGA_ANTECIPADA.getDesc().contains(tipo)) {
			return DI_ENTREGA_ANTECIPADA;
		}
		if (PROCESSO_ADM.getDesc().contains(tipo)) {
			return PROCESSO_ADM;
		}
		if (DTC.getDesc().contains(tipo)) {
			return DTC;
		}

		if ("DTA_LCL".equalsIgnoreCase(tipo)) {
			return DTA_LCL;
		}

		if ("DTA_MASTER".equalsIgnoreCase(tipo)) {
			return DTA_MASTER;
		}

		if ("DTA_YARD".equalsIgnoreCase(tipo)) {
			return DTA_PATIO;
		}

		if ("DTA_STORAGE".equalsIgnoreCase(tipo)) {
			return DTA_ARMAZENAMENTO;
		}

		if ("DTA_SIMPLIFIED".equalsIgnoreCase(tipo)) {
			return DTA_SIMPLIFICADA;
		}

		final EnTipoProcessoFaturamento status = buscarPorDescricao(tipo);
		if(status != null)
			return status;

		try {
			return valueOf(tipo);
		} catch (final Exception e) {
			throw new WSFaturamentoFastException("Tipo de Processo Faturamento: " + tipo + ", inválido!");
		}
	}
}
