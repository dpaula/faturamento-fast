package com.portoitapoa.faturamentofast.vo;

import com.portoitapoa.faturamentofast.enuns.EnStatusSolicitacao;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static io.swagger.v3.oas.annotations.media.Schema.AccessMode.READ_WRITE;

/**
 * @author Dionízio Inácio
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "SolicitacaoVO")
public class SolicitacaoVO {

    @Schema(accessMode = READ_WRITE, example = "6a0f69fc-63c3-48e9-9f0a-efde73604d69", description = "Id")
    private UUID id;
    @Schema(accessMode = READ_WRITE, example = "", description = "faturarAteda solicitação")
    private String solicitante;
    @Schema(accessMode = READ_WRITE, example = "", description = "navio da solicitação")
    private String navio;
    @Schema(accessMode = READ_WRITE, example = "", description = "processo da solicitação")
    private String processo;
    @Schema(accessMode = READ_WRITE, example = "RASCUNHO", description = "status da solicitação")
    private EnStatusSolicitacao status;
    @Schema(accessMode = READ_WRITE, example = "", description = "nomeImportador da solicitação")
    private String nomeImportador;
    @Schema(accessMode = READ_WRITE, example = "", description = "ce da solicitação")
    private String ce;
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
    private String cpfCnpjEmpresa;
    @Schema(accessMode = READ_WRITE, example = "", description = "faturarAte da solicitação")
    private LocalDate faturarAte;
    @Schema(accessMode = READ_WRITE, example = "", description = "faturarPara da solicitação")
    private String faturarPara;
    @Schema(accessMode = READ_WRITE, example = "", description = "emailsEnvioFaturamento da solicitação")
    private String emailsEnvioFaturamento;
    @Schema(accessMode = READ_WRITE, example = "", description = "indica se a solicitação tem divergencia")
    private Boolean possuiDivergencias;
    @Schema(accessMode = READ_WRITE, example = "", description = "divergencia da solicitação")
    private String divergencia;
    @Schema(accessMode = READ_WRITE, example = "", description = "emailsEnvioLiberacaoDocumental da solicitação")
    private String emailsEnvioLiberacaoDocumental;
    @Schema(accessMode = READ_WRITE, example = "", description = "Quem enviou para liberação")
    private String enviadoParaLiberacaoPor;
    @Schema(accessMode = READ_WRITE, example = "", description = "Liberador da solicitação")
    private String liberadoPor;
    @Schema(accessMode = READ_WRITE, example = "", description = "Criador da solicitação")
    private String criadoPor;
    @Schema(accessMode = READ_WRITE, example = "", description = "Analisador da solicitação")
    private String analisandoPor;
    @Schema(accessMode = READ_WRITE, example = "", description = "Liberador da solicitação")
    private String cnpjAdquirente;

}
