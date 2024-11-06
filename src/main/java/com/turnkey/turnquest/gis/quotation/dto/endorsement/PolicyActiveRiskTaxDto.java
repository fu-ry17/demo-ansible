package com.turnkey.turnquest.gis.quotation.dto.endorsement;

import java.math.BigDecimal;

public record PolicyActiveRiskTaxDto(
        Long taxRateId,
        BigDecimal taxRate,
        BigDecimal divisionFactor,
        BigDecimal taxAmount,
        String transactionTypeCode,
        String taxDescription,
        String taxType,
        String applicationArea,
        Long productSubclassId
){}
