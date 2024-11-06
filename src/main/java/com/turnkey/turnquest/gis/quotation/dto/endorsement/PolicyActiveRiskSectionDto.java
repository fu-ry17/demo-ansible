package com.turnkey.turnquest.gis.quotation.dto.endorsement;

import com.turnkey.turnquest.gis.quotation.enums.PremiumRateType;
import com.turnkey.turnquest.gis.quotation.enums.SectionType;

import java.math.BigDecimal;

public record PolicyActiveRiskSectionDto (
        Long sectionId,
        String sectionDescription,
        String sectionType,
        BigDecimal freeLimitAmount,
        BigDecimal limitAmount,
        BigDecimal usedLimitAmount,
        BigDecimal premiumAmount,
        BigDecimal minimumPremiumAmount,
        BigDecimal premiumRate,
        BigDecimal rateDivisionFactor,
        String prorated,
        String premiumRateDescription,

        PremiumRateType rateType,
        Long subClassSectionId,
        BigDecimal multiplierDivisionFactor,
        String subClassSectionDesc
    ) {}
