package com.portoitapoa.faturamentofast.repository;

import com.portoitapoa.faturamentofast.entity.ImportacaoDeparted;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.Tuple;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * @author Alan Lopes
 */
public interface ImportacaoDepartedRepository extends JpaRepository<ImportacaoDeparted, BigInteger> {


    @Query(value = "SELECT d.numero_bl AS numeroBl, d.doc_aduaneiro_tipo AS docTipoAduaneiro  from vw_msFaturamento_Imprt_Departed d where 1 = 1 and d.numero_bl is not null", nativeQuery = true)
    Set<Tuple> findAllNumerosBl();

    @Query(value = "SELECT d.numero_bl AS numeroBl, d.doc_aduaneiro_tipo AS docTipoAduaneiro  from vw_msFaturamento_Imprt_Departed d where 1 = 1 and d.numero_bl is not null and d.data_insercao >= :data", nativeQuery = true)
    Set<Tuple> findAllNumerosBlPorHoras(@Param("data") LocalDateTime data);
}
