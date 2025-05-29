package com.portoitapoa.faturamentofast.repository;

import com.portoitapoa.faturamentofast.entity.Exportacao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * @author Fernando de Lima
 */
public interface ExportacaoRepository extends JpaRepository<Exportacao, BigInteger> {

    Optional<Exportacao> findFirstByCnpjExportadorAndNavioAndBooking(String cnpjExportador, String navio,
        String booking);

    Optional<Exportacao> findFirstByCnpjExportadorContainingAndNavioAndBooking(String radical, String navio,
        String booking);

    List<Exportacao> findAllByCnpjExportadorContainingAndNavioAndDataFaturamento(String cnpjExportador, String navio,
        LocalDateTime dataFaturamento);

    Page<Exportacao> findAllByCnpjExportador(String cnpjExportador, Pageable pageable);

    Page<Exportacao> findAllByNavio(String navio, Pageable pageable);

    Page<Exportacao> findAllByBooking(String booking, Pageable pageable);

    Page<Exportacao> findAllByCnpjExportadorAndNavio(String cnpjExportador, String navio, Pageable pageable);

    Page<Exportacao> findAllByCnpjExportadorAndBooking(String cnpjExportador, String booking, Pageable pageable);

    Page<Exportacao> findAllByNavioAndBooking(String navio, String booking, Pageable pageable);

    Page<Exportacao> findAllByCnpjExportadorAndNavioAndBooking(String cnpjExportador, String navio, String booking,
        Pageable pageable);

//    List<Exportacao> findAllByDataFaturamentoIsGreaterThanEqualAndCnpjExportadorNotNullAndNavioNotNullAndIsCabotIsFalse(
//        LocalDateTime dataFaturamento);

    List<Exportacao> findAllByDataFaturamentoIsGreaterThanEqualAndCnpjExportadorNotNullAndNavioNotNull(
        LocalDateTime dataFaturamento);

    List<Exportacao> findAllByDataFaturamentoIsGreaterThanEqualAndCnpjExportadorNotNullAndNavio(
            LocalDateTime dataFaturamento, String navio);
}
