package com.portoitapoa.faturamentofast.model;

import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Fernando de Lima
 *
 */
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@ToString
public class Container {

    @EqualsAndHashCode.Include
    private String descricao;

    private LocalDate dataLimite;

    @Builder.Default
    private final Set<Cue> eventos = new HashSet<>();
}
