package com.turnkey.turnquest.gis.quotation.dto.quotation;

import com.turnkey.turnquest.gis.quotation.enums.BenefitType;
import com.turnkey.turnquest.gis.quotation.enums.PremiumRateType;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class QuotationRiskSectionDto {

    private Long id;

    private String description;

    private Long foreignId;

    private Long sectionId;

    private String sectionCode;

    private Long subClassSectionId;

    private String subClassSectionDesc;

    private String sectionType;

    private Long quotationRiskId;

    private BigDecimal limitAmount;

    private BigDecimal annualPremiumAmount;

    private BigDecimal premiumAmount;

    private BigDecimal minimumPremiumAmount;

    private BigDecimal usedLimitAmount;

    private BigDecimal freeLimitAmount = BigDecimal.ZERO;

    private BigDecimal premiumRate;

    private BigDecimal multiplierRate;

    private BigDecimal multiplierDivisionFactor;

    private BigDecimal rateDivisionFactor;

    private PremiumRateType rateType = PremiumRateType.FXD;

    private String prorated;

    private String premiumRateDescription;

    private BigDecimal calculationGroup;

    private BigDecimal rowNumber;

    private String compute;

    private BenefitType benefitType;

    private String dualBasis;

    private BigDecimal sumInsuredRate;

    private String sumInsuredLimitType;

    private String sectionMandatory;
}
