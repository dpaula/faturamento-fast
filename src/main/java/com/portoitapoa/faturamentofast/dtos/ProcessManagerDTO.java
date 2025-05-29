package com.portoitapoa.faturamentofast.dtos;

import com.portoitapoa.faturamentofast.enuns.EnManagerStatusType;

import java.time.LocalDateTime;
import java.util.UUID;

public record ProcessManagerDTO(
        UUID id,
        UUID preImportId,
        String ceNumber,
        String blNumber,
        String number,
        Boolean isPart,
        String type,
        EnManagerStatusType status,
        String federationUnit,
        LocalDateTime registerDate,
        LocalDateTime reviewSubmissionDate,
        LocalDateTime releaseDate,
        String dispatcher,
        String dispatcherLogin,
        String dispatcherName,
        String customPrecinct,
        String importer,
        String importerName,
        String carrier,
        String carrierName,
        String responsibleProcessUser,
        String responsibleProcessUsername,
        LocalDateTime revisionStartDate,
        String trip,
        String channel,
        Integer containerCount,
        LocalDateTime clearanceDate,
        String purchaser,
        String purchaserName,
        String billFor,
        String billForName,
        String destination,
        Boolean hasCommercialAgreement,
        String pdfRelease,
        String sefazStatus,
        String siscomexStatus,
        Boolean isConsolidated
) {
}
