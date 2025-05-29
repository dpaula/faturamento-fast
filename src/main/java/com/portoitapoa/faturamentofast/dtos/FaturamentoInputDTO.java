package com.portoitapoa.faturamentofast.dtos;

import com.portoitapoa.faturamentofast.entity.Siscomex;
import com.portoitapoa.faturamentofast.enuns.EnStatusEnvioFatOnline;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class FaturamentoInputDTO {
    private Integer oid;
    private String di;
    private String tipo;
    private String tipoSelecionado;
    private String blOriginal;
    private String bls;
    private String nomeFaturarPara;
    private String cnpjFaturarPara;
    private String nomeAdquirente;
    private String cnpjAdquirente;
    private LocalDateTime dataLiberacao;
    private LocalDate dataFaturamento;
    private String cnpjDespachante;
    private String emailsFaturamento;
    private LocalDateTime dataEnvioFatOnline;
    private LocalDateTime dataLiberacaoFatOnline;
    private Boolean parte;
    private String status;
    private EnStatusEnvioFatOnline statusEnvioFatOnline;
    private String navio;
    private List<Long> gkeyServicosExcecaoCockPit = new ArrayList<>();
    private Boolean crossdocking;
    private String ce;

    public static FaturamentoInputDTO fromSiscomex(Siscomex siscomex) {
        FaturamentoInputDTO input = new FaturamentoInputDTO();
        input.setOid(siscomex.getOid());
        input.setDi(siscomex.getDi());
        input.setTipo(siscomex.getTipo());
        input.setTipoSelecionado(siscomex.getTipoSelecionado());
        input.setBlOriginal(siscomex.getBlOriginal());
        input.setBls(siscomex.getBls());
        input.setNomeFaturarPara(siscomex.getNomeFaturarPara());
        input.setCnpjFaturarPara(siscomex.getCnpjFaturarPara());
        input.setNomeAdquirente(siscomex.getNomeAdquirente());
        input.setCnpjAdquirente(siscomex.getCnpjAdquirente());
        input.setDataLiberacao(siscomex.getDataLiberacao());
        input.setDataFaturamento(siscomex.getDataFaturamento());
        input.setCnpjDespachante(siscomex.getCnpjDespachante());
        input.setEmailsFaturamento(siscomex.getEmailsFaturamento());
        input.setDataEnvioFatOnline(siscomex.getDataEnvioFatOnline());
        input.setDataLiberacaoFatOnline(siscomex.getDataLiberacaoFatOnline());
        input.setParte(siscomex.getParte());
        input.setStatus(siscomex.getStatus());
        input.setStatusEnvioFatOnline(siscomex.getStatusEnvioFatOnline());
        input.setNavio(siscomex.getNavio());
        input.setCrossdocking(siscomex.getCrossdocking());
        return input;
    }

    public static FaturamentoInputDTO fromManagerDTO(ProcessManagerDTO processManager) {
        FaturamentoInputDTO input = new FaturamentoInputDTO();
        input.setNavio(processManager.trip());
        input.setNomeAdquirente(processManager.purchaserName());
        input.setCnpjAdquirente(processManager.purchaser());
        input.setDataLiberacao(processManager.registerDate());
        input.setDataFaturamento(processManager.releaseDate() == null
                ? LocalDate.now()
                : processManager.releaseDate().toLocalDate());
        input.setCnpjDespachante(processManager.dispatcher());
        input.setParte(processManager.isPart());
        input.setBlOriginal(processManager.blNumber());
        input.setBls(processManager.blNumber());
        input.setCe(processManager.ceNumber());
        input.setCnpjFaturarPara(processManager.billFor());
        input.setCrossdocking(false);
        input.setTipo(processManager.type());
        input.setDi(processManager.number());
        return input;
    }

}