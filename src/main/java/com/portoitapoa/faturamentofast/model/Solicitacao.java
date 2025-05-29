package com.portoitapoa.faturamentofast.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.portoitapoa.faturamentofast.vo.SolicitacaoVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static io.swagger.v3.oas.annotations.media.Schema.AccessMode.READ_WRITE;

/**
 * @author Dionízio Inácio
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class Solicitacao {

    @Schema(accessMode = READ_WRITE, example = "6a0f69fc-63c3-48e9-9f0a-efde73604d69", description = "Id")
    private UUID id;
    @Schema(accessMode = READ_WRITE, example = "", description = "faturarAteda solicitação")
    private String solicitante; //faturarPara
    @Schema(accessMode = READ_WRITE, example = "", description = "navio da solicitação")
    private String navio; //campo obrigatorio
    @Schema(accessMode = READ_WRITE, example = "", description = "processo da solicitação")
    private String processo; //NO FATURAMENTO É LONG campo obrigatório
    @Schema(accessMode = READ_WRITE, example = "", description = "nomeImportador da solicitação")
    private String nomeImportador; //campo obrigatório
    @Schema(accessMode = READ_WRITE, example = "", description = "ce da solicitação")
    private String ce;//campo obrigatório
    @Schema(accessMode = READ_WRITE, example = "", description = "analise da solicitação")
    private String analise;
    @Schema(accessMode = READ_WRITE, example = "", description = "criadoEm da solicitação")
    private LocalDateTime criadoEm;
    @Schema(accessMode = READ_WRITE, example = "", description = "liberadoEm da solicitação")
    private LocalDateTime liberadoEm;
    @Schema(accessMode = READ_WRITE, example = "", description = "enviadoParaLiberacaoEm da solicitação")
    private LocalDateTime enviadoParaLiberacaoEm;
    @Schema(accessMode = READ_WRITE, example = "", description = "analisandoEm da solicitação")
    private LocalDateTime analisandoEm;
    @Schema(accessMode = READ_WRITE, example = "", description = "cpfCnpjEmpresa da solicitação")
    private String cpfCnpjEmpresa;//campo obrigatório
    @Schema(accessMode = READ_WRITE, example = "", description = "faturarAte da solicitação")
    private LocalDate faturarAte;
    @Schema(accessMode = READ_WRITE, example = "", description = "faturarPara da solicitação")
    private String faturarPara;//campo obrigatório
    @Schema(accessMode = READ_WRITE, example = "", description = "emailsEnvioFaturamento da solicitação")
    private String emailsEnvioFaturamento;//campo obrigatório
    @Schema(accessMode = READ_WRITE, example = "", description = "indica se a solicitação tem divergencia")
    private Boolean possuiDivergencias;
    @Schema(accessMode = READ_WRITE, example = "", description = "divergencia da solicitação")
    private String divergencia;
    @Schema(accessMode = READ_WRITE, example = "", description = "Quem enviou para liberação")
    private String enviadoParaLiberacaoPor;
    @Schema(accessMode = READ_WRITE, example = "", description = "Liberador da solicitação")
    private String liberadoPor;
    @Schema(accessMode = READ_WRITE, example = "", description = "Criador da solicitação")
    private String criadoPor;
    @Schema(accessMode = READ_WRITE, example = "", description = "Analisador da solicitação")
    private String analisandoPor;
   @Schema(accessMode = READ_WRITE, example = "", description = "Protocolo da solicitação")
   private Long protocolo;
    @Schema(accessMode = READ_WRITE, example = "", description = "Autorização de avarias da solicitação")
    private Boolean autorizaAvaria;
    @Schema(accessMode = READ_WRITE, example = "", description = "Liberador da solicitação")
    private String cnpjAdquirente;//campo obrigatório
    @Schema(example = "NBOIOA19121958", description =  "Número do BL do faturamento")
    @Size(max = 60)
    private String numeroBl;

    public Solicitacao(SolicitacaoVO solicitacaoVO) {
        this.id = solicitacaoVO.getId();
        this.solicitante = solicitacaoVO.getSolicitante();
        this.navio = solicitacaoVO.getNavio();
        this.processo = solicitacaoVO.getProcesso();
        this.nomeImportador = solicitacaoVO.getNomeImportador();
        this.ce = solicitacaoVO.getCe();
        this.analise = solicitacaoVO.getAnalise();
        this.criadoEm = solicitacaoVO.getCriadoEm();
        this.liberadoEm = solicitacaoVO.getLiberadoEm();
        this.enviadoParaLiberacaoEm = solicitacaoVO.getEnviadoParaLiberacaoEm();
        this.analisandoEm = solicitacaoVO.getAnalisandoEm();
        this.cpfCnpjEmpresa = solicitacaoVO.getCpfCnpjEmpresa();
        this.faturarAte = solicitacaoVO.getFaturarAte();
        this.faturarPara = solicitacaoVO.getFaturarPara();
        this.emailsEnvioFaturamento = solicitacaoVO.getEmailsEnvioFaturamento();
        this.possuiDivergencias = solicitacaoVO.getPossuiDivergencias();
        this.divergencia = solicitacaoVO.getDivergencia();
        this.enviadoParaLiberacaoPor = solicitacaoVO.getEnviadoParaLiberacaoPor();
        this.liberadoPor = solicitacaoVO.getLiberadoPor();
        this.criadoPor = solicitacaoVO.getCriadoPor();
        this.analisandoPor = solicitacaoVO.getAnalisandoPor();
        this.cnpjAdquirente = solicitacaoVO.getCnpjAdquirente();
        this.numeroBl = solicitacaoVO.getProcesso();
    }
}
