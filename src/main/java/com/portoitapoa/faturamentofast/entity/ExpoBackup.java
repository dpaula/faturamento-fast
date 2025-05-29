package com.portoitapoa.faturamentofast.entity;

import com.portoitapoa.faturamentofast.enuns.EnStatusEnvioFatOnline;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author Fernando de Lima
 */
@Data
@Entity(name = "expo_backup")
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExpoBackup {

    @Id
    @Type(type = "uuid-char")
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "generator", strategy = "uuid2")
    @Column(nullable = false, columnDefinition = "uniqueidentifier")
    @EqualsAndHashCode.Include
    private UUID id;

    @Column(name = "cnpj_exportador")
    private String cnpjExportador;

    private String bookings;

    private String navio;

    @Column(name = "gkey_cues")
    private String gkeysCues;

    @Column(name = "data_faturamento")
    private LocalDateTime dataFaturamento;

    @Column(name = "data_envio_fat_online")
    private LocalDateTime dataEnvioFatOnline;

    @Column(name = "faturar_por_booking")
    private boolean faturarPorBooking;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_envio_fat_online")
    private EnStatusEnvioFatOnline statusEnvioFatOnline;
}