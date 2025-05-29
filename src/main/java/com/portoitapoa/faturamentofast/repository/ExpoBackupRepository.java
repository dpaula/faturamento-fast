package com.portoitapoa.faturamentofast.repository;

import com.portoitapoa.faturamentofast.entity.ExpoBackup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * @author Fernando de Lima
 */
public interface ExpoBackupRepository extends JpaRepository<ExpoBackup, UUID> {

    boolean existsByCnpjExportadorAndNavioAndBookingsContaining(String cnpjExportador, String navio, String bookings);
}
