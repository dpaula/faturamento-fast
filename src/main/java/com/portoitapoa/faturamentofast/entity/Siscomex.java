package com.portoitapoa.faturamentofast.entity;

import com.portoitapoa.faturamentofast.enuns.EnStatusEnvioFatOnline;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Fernando de Lima
 *
 */
@Data
@Entity
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Siscomex {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
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

    @Enumerated(EnumType.STRING)
    private EnStatusEnvioFatOnline statusEnvioFatOnline;

    @Transient
    private String navio;

    @Transient
    @Builder.Default
    private List<Long> gkeyServicosExcecaoCockPit = new ArrayList<>();

    private Boolean crossdocking;

    public Boolean getCrossdocking(){
        return crossdocking != null && crossdocking;
    }

    public Boolean getParte(){
        return parte != null && parte;
    }


}