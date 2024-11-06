package com.turnkey.turnquest.gis.quotation.dto.quotation;

import com.turnkey.turnquest.gis.quotation.enums.BenefitType;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class QuickQuotationRiskSectionDto {
    private Long subClassSectionId;

    private BigDecimal limitAmount;

    private BigDecimal rate = BigDecimal.ZERO;

    private BenefitType benefitType = BenefitType.LIMIT;
}
