package com.turnkey.turnquest.gis.quotation.dto.billing;

import java.math.BigDecimal;
import java.util.List;

public record ConversionDto(
        String paymentRef,
        String policyNumber,
        BigDecimal paidAmount,
        String paymentFrequency,
        List<Long> installmentNumber,
        String installmentRef,

        String policyStatus
) {
}
