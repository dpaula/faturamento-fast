package com.portoitapoa.faturamentofast.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.portoitapoa.faturamentofast.enuns.EnCategoriaFaturamento;
import com.portoitapoa.faturamentofast.model.Container;
import com.portoitapoa.faturamentofast.model.FaturamentoExportacao;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.ArrayList;
import java.util.List;

import static io.swagger.v3.oas.annotations.media.Schema.AccessMode.READ_ONLY;

/**
 * @author Fernando de Lima
 */
@Schema(name = "FaturamentoExportacao")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FaturamentoExportacaoVO {

    @Schema(accessMode = READ_ONLY, description = "Processos DUES")
    private List<ProcessoVO> processos = new ArrayList<>();

    @Schema(accessMode = READ_ONLY, example = "VSSEL13", description = "Identificação do navio")
    private String navio;

    @Schema(accessMode = READ_ONLY, example = "EXPORTACAO", description = "Categoria declarativa do processo")
    @Enumerated(EnumType.STRING)
    private EnCategoriaFaturamento categoriaFaturamento;

    @Schema(accessMode = READ_ONLY, description = "Cliente do Faturamento")
    private ClienteVO cliente;

    @Schema(accessMode = READ_ONLY, example = "['BOOKING1', 'BOOKING2']", description = "Bookings do processo de Exportação")
    private List<String> bookings;

    @Schema(accessMode = READ_ONLY, description = "Gkey dos Serviços Enviados para CockPit")
    private List<Long> gkeyServicosExcecaoCockPit;

    @Schema(accessMode = READ_ONLY, description = "Se é um faturamento de Cabotagem Exportação")
    private Boolean isCabot;

    @Schema(accessMode = READ_ONLY, description = "Identifica se é faturamento de container Yard.")
    private Boolean yard;

    @Schema(accessMode = READ_ONLY, description = "Lista de containers do processo")
    private List<Container> containers = new ArrayList<>();

    public static FaturamentoExportacaoVO parse(final FaturamentoExportacao exportacao) {
        return FaturamentoExportacaoVO.builder()
            .processos(exportacao.getProcessos())
            .navio(exportacao.getNavio())
            .categoriaFaturamento(exportacao.getCategoriaFaturamento())
            .cliente(exportacao.getCliente())
            .bookings(exportacao.getBookings())
            .gkeyServicosExcecaoCockPit(exportacao.getGkeyServicosExcecaoCockPit())
            .isCabot(exportacao.isCabot())
            .yard(exportacao.isYard())
            .build();
    }

    public static FaturamentoExportacaoVO parseYard(final FaturamentoExportacao exportacao) {
        return FaturamentoExportacaoVO.builder()
                .processos(exportacao.getProcessos())
                .navio(exportacao.getNavio())
                .categoriaFaturamento(exportacao.getCategoriaFaturamento())
                .cliente(exportacao.getCliente())
                .bookings(exportacao.getBookings())
                .gkeyServicosExcecaoCockPit(exportacao.getGkeyServicosExcecaoCockPit())
                .isCabot(exportacao.isCabot())
                .yard(exportacao.isYard())
                .containers(exportacao.getContainers())
                .build();
    }
}
