package com.portoitapoa.faturamentofast.repository;

import com.portoitapoa.faturamentofast.entity.ExportacaoCabot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * @author Dionízio Inácio
 */
public interface ExportacaoCabotRepository extends JpaRepository<ExportacaoCabot, BigInteger> {

    List<ExportacaoCabot> findAllByDataFaturamentoIsGreaterThanEqualAndCnpjFatCabotagemNotNullAndNavioNotNullAndIsCabotIsTrue(
        LocalDateTime dataFaturamento);

    List<ExportacaoCabot> findAllByDataFaturamentoIsGreaterThanEqualAndCnpjExportadorNotNullAndNavioNotNull(
        LocalDateTime dataFaturamento);

    Optional<ExportacaoCabot> findFirstByCnpjExportadorAndNavioAndBooking(String cnpjExportador, String navio, String booking);

    List<ExportacaoCabot> findAllByCnpjExportadorContainingAndNavioAndDataFaturamento(String cnpjExportador, String navio,
        LocalDateTime dataFaturamento);
}
