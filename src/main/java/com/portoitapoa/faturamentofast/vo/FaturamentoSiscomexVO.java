package com.portoitapoa.faturamentofast.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.portoitapoa.faturamentofast.dtos.FaturamentoInputDTO;
import com.portoitapoa.faturamentofast.dtos.ProcessManagerDTO;
import com.portoitapoa.faturamentofast.entity.Siscomex;
import com.portoitapoa.faturamentofast.enuns.EnCategoriaFaturamento;
import com.portoitapoa.faturamentofast.enuns.EnTipoPagamentoFaturamento;
import com.portoitapoa.faturamentofast.enuns.EnTipoProcessoFaturamento;
import com.portoitapoa.faturamentofast.model.Solicitacao;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static io.swagger.v3.oas.annotations.media.Schema.AccessMode.READ_ONLY;

/**
 * @author Fernando de Lima
 * @author Dionízio Inácio
 */
@Schema(name = "FaturamentoSiscomex")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FaturamentoSiscomexVO {

    @Schema(accessMode = READ_ONLY, example = "14334", description = "Protocolo do Faturamento")
    private Long protocolo;

    @Schema(accessMode = READ_ONLY, description = "Processos de Documentos")
    private List<ProcessoVO> processos = new ArrayList<>();

    @Schema(accessMode = READ_ONLY, example = "*SUDUA5BMWSA0636X", description = "Número do BL do faturamento")
    private String numeroBl;

    @Schema(accessMode = READ_ONLY, example = "VSSEL13", description = "Identificação do navio")
    private String navio;

    @Schema(accessMode = READ_ONLY, example = "IMPORTACAO", description = "Categoria declarativa do processo")
    @Enumerated(EnumType.STRING)
    private EnCategoriaFaturamento categoriaFaturamento;

    @Schema(accessMode = READ_ONLY, description = "Cliente do Faturamento")
    private ClienteVO cliente;

    @Schema(accessMode = READ_ONLY, example = "Marcelo da Matta", description = "Nome do dono da mercadoria")
    private String nomeAdquirente;

    @Schema(accessMode = READ_ONLY, example = "80663958000100", description = "CNPJ do dono da mercadoria")
    private String cnpjAdquirente;

    @Schema(accessMode = READ_ONLY, example = "2020-02-24T15:54:35.474025", description = "Data que foi efetuado a leitura do processo via siscomax")
    private LocalDateTime dataLiberacao;

    @Schema(accessMode = READ_ONLY, example = "2020-01-01", description = "Data que define até quando sera pago a armazenagem do container")
    private LocalDate dataFaturarAte;

    @Schema(accessMode = READ_ONLY, example = "99431296000162", description = "Representante legal do cliente, responsável por liberar a mercadoria para o cliente pagador ")
    private String cnpjDespachante;

    @Schema(accessMode = READ_ONLY, example = "felipe.correa@portoitapoa.com", description = "Emails para notificação durante o processo do faturamento")
    private String emails;

    @Schema(accessMode = READ_ONLY, description = "Gkey dos Serviços Enviados para CockPit")
    private List<Long> gkeyServicosExcecaoCockPit;

    @Schema(accessMode = READ_ONLY, description = "Sinaliza se o Faturamento Siscomex possui crossdocking")
    private Boolean crossdocking;

    @Schema(accessMode = READ_ONLY, description = "Tipo da condição de pagamento")
    private EnTipoPagamentoFaturamento condicaoPagamento;

    @Schema(accessMode = READ_ONLY, example = "true", description =  "Indica se o faturamento é parte ou não")
    private boolean parte;

    @Schema(accessMode = READ_ONLY, example = "9600738237;1610738885", description = "Retorna os bls separados por ; caso o processo seja parte")
    private String bls;

    public static FaturamentoSiscomexVO parse(final Siscomex siscomex) {
        return FaturamentoSiscomexVO.builder()
                .protocolo(siscomex.getOid().longValue())
                .processos(getProcessosSiscomex(siscomex.getDi(), siscomex.getTipo()))
                .numeroBl(siscomex.getBlOriginal())
                .categoriaFaturamento(EnCategoriaFaturamento.IMPORTACAO)
                .cliente(getClienteSiscomex(siscomex.getNomeFaturarPara(), siscomex.getCnpjFaturarPara()))
                .nomeAdquirente(siscomex.getNomeAdquirente())
                .cnpjAdquirente(siscomex.getCnpjAdquirente())
                .dataLiberacao(siscomex.getDataLiberacao())
                .dataFaturarAte(siscomex.getDataFaturamento())
                .cnpjDespachante(siscomex.getCnpjDespachante())
                .emails(siscomex.getEmailsFaturamento())
                .navio(siscomex.getNavio())
                .gkeyServicosExcecaoCockPit(
                        !siscomex.getGkeyServicosExcecaoCockPit().isEmpty() ? siscomex.getGkeyServicosExcecaoCockPit() : null)
                .crossdocking(siscomex.getCrossdocking())
                .parte(siscomex.getParte())
                .bls(siscomex.getBls())
                .build();
    }

    public static FaturamentoSiscomexVO parse(final SolicitacaoVO vo) {
        return FaturamentoSiscomexVO.builder()
                .numeroBl(vo.getProcesso())
                .categoriaFaturamento(EnCategoriaFaturamento.IMPORTACAO_CABOTAGEM)
                .cliente(getClienteCabotagemImpo(vo))
                .nomeAdquirente(vo.getLiberadoPor()) //Quem liberou a solicitacao
                .cnpjAdquirente(vo.getCnpjAdquirente())
                .dataLiberacao(vo.getLiberadoEm())
                .dataFaturarAte(vo.getFaturarAte())
                .cnpjDespachante(vo.getCnpjAdquirente())
                .emails(vo.getEmailsEnvioFaturamento())
                .navio(vo.getNavio())
                .build();
    }

    private static ClienteVO getClienteSiscomex(final String nomeFaturarPara, final String cnpjFaturarPara) {
        if (StringUtils.isBlank(cnpjFaturarPara) && StringUtils.isBlank(nomeFaturarPara)) {
            return null;
        }

        return ClienteVO.builder()
                .nome(getNomeCliente(nomeFaturarPara))
                .cnpj(getCnpjCliente(cnpjFaturarPara))
                .build();
    }

    private static ClienteVO getClienteCabotagemImpo(final SolicitacaoVO vo) {
        if (StringUtils.isBlank(vo.getFaturarPara()) && StringUtils.isBlank(vo.getNomeImportador())) {
            return null;
        }

        return ClienteVO.builder()
                .nome(getNomeCliente(vo.getNomeImportador()))
                .cnpj(getCnpjCliente(vo.getFaturarPara()))
                .build();
    }

    private static String getCnpjCliente(final String cnpjFaturarPara) {

        if (StringUtils.isBlank(cnpjFaturarPara)) {
            return null;
        }

        return cnpjFaturarPara.trim();
    }

    private static String getNomeCliente(final String nomeFaturarPara) {

        if (StringUtils.isBlank(nomeFaturarPara)) {
            return null;
        }

        return nomeFaturarPara.trim();
    }

    private static List<ProcessoVO> getProcessos(final String di, final String ce, final String tipo) {
        if("DTC".equals(tipo)) {
            return getProcessosDTC(ce, tipo);
        }

        return getProcessosSiscomex(di, tipo);
    }

    private static List<ProcessoVO> getProcessosDTC(final String ce, final String tipo) {
        final var processo = ProcessoVO.builder()
                .tipo(EnTipoProcessoFaturamento.valueFromDescTipo(tipo))
                .numero(ce)
                .build();

        return List.of(processo);
    }

    private static List<ProcessoVO> getProcessosSiscomex(final String di, final String tipo) {
        if (StringUtils.isBlank(di) && StringUtils.isBlank(tipo)) {
            return List.of();
        }

        final var processo = ProcessoVO.builder()
                .tipo(EnTipoProcessoFaturamento.valueFromDescTipo(tipo))
                .numero(di)
                .build();

        return List.of(processo);
    }

    public static FaturamentoSiscomexVO parse(Solicitacao solicitacao) {
        ClienteVO cliente = ClienteVO.builder()
                .nome(solicitacao.getNomeImportador()
                        .trim())
                .cnpj(solicitacao.getFaturarPara()
                        .trim())
                .build();
        return FaturamentoSiscomexVO.builder()
                .numeroBl(solicitacao.getProcesso())
                .categoriaFaturamento(EnCategoriaFaturamento.IMPORTACAO_CABOTAGEM)
                .cliente(cliente)
                .nomeAdquirente(solicitacao.getLiberadoPor())
                .cnpjAdquirente(solicitacao.getCnpjAdquirente())
                .dataLiberacao(solicitacao.getLiberadoEm())
                .dataFaturarAte(solicitacao.getFaturarAte())
                .cnpjDespachante(solicitacao.getCnpjAdquirente())
                .emails(solicitacao.getEmailsEnvioFaturamento())
                .navio(solicitacao.getNavio())
                .build();
    }

    public static FaturamentoSiscomexVO parse(ProcessManagerDTO process) {
        ClienteVO cliente = ClienteVO.builder()
                .nome(process.billForName())
                .cnpj(process.billFor().trim())
                .build();

        return FaturamentoSiscomexVO.builder()
                .bls(process.blNumber())
                .navio(process.trip())
                .processos(getProcessos(process.number(), process.ceNumber(), process.type()))
                .categoriaFaturamento(EnCategoriaFaturamento.IMPORTACAO)
                .cliente(cliente)
                .nomeAdquirente(process.purchaserName())
                .cnpjAdquirente(process.purchaser())
                .dataLiberacao(process.registerDate())
                .dataFaturarAte(process.releaseDate() == null ? null : process.releaseDate().toLocalDate()) // Analisar se é o mesmo que dataLiberacao
                .cnpjDespachante(process.dispatcher())
                .parte(process.isPart())
                .build();
    }

    public static FaturamentoSiscomexVO parse(FaturamentoInputDTO input) {
        return FaturamentoSiscomexVO.builder()
                .protocolo(input.getOid() == null ? null : input.getOid().longValue())
                .processos(getProcessos(input.getDi(), input.getCe(), input.getTipo()))
                .numeroBl(input.getBlOriginal())
                .categoriaFaturamento(EnCategoriaFaturamento.IMPORTACAO)
                .cliente(getClienteSiscomex(input.getNomeFaturarPara(), input.getCnpjFaturarPara()))
                .nomeAdquirente(input.getNomeAdquirente())
                .cnpjAdquirente(input.getCnpjAdquirente())
                .dataLiberacao(input.getDataLiberacao())
                .dataFaturarAte(input.getDataFaturamento())
                .cnpjDespachante(input.getCnpjDespachante())
                .emails(input.getEmailsFaturamento())
                .navio(input.getNavio())
                .gkeyServicosExcecaoCockPit(
                        input.getGkeyServicosExcecaoCockPit() != null && !input.getGkeyServicosExcecaoCockPit().isEmpty() ? input.getGkeyServicosExcecaoCockPit() : null
                )
                .crossdocking(input.getCrossdocking())
                .parte(input.getParte())
                .bls(input.getBls())
                .build();
    }

    public boolean valido() {
        if (processos == null || processos.isEmpty()) {
            return false;
        }

        if (processos.get(0).getNumero() == null || processos.get(0).getTipo() == null) {
            return false;
        }

        if (EnCategoriaFaturamento.IMPORTACAO.equals(categoriaFaturamento) && StringUtils.isBlank(numeroBl)) {
            return false;
        }

        return cliente != null && cliente.getCnpj() != null && cliente.getNome() != null;
    }
}
