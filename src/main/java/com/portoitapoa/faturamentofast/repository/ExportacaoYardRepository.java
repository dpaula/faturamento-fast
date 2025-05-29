package com.portoitapoa.faturamentofast.repository;

import com.portoitapoa.faturamentofast.entity.ExportacaoYard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Fernando de Lima
 */
public interface ExportacaoYardRepository extends JpaRepository<ExportacaoYard, BigInteger> {

    @Query("SELECT yard FROM vw_msFaturamento_Exprt_Cross_Yard yard WHERE yard.dataLiberacaoFaturamento >= :dataLiberacaoFaturamento AND yard.cnpjExportador IS NOT NULL")
    List<ExportacaoYard> filtroPorDataExportacao(@Param("dataLiberacaoFaturamento") LocalDateTime dataLiberacaoFaturamento);

}
