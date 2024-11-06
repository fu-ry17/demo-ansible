package com.turnkey.turnquest.gis.quotation.dto.underwriting;

import com.turnkey.turnquest.gis.quotation.enums.BenefitType;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class PolicyRiskSectionDto implements Serializable {

    private Long id;

    private String code;

    private Long policyRiskId;

    private String description;

    private Long sectionId;

    private Long subClassSectionId;

    private String subClassSectionDesc;

    private Integer rowNumber;

    private BigDecimal calculationGroup;

    private String prorated;

    private BigDecimal limitAmount;

    private BigDecimal freeLimitAmount;

    private BigDecimal usedLimitAmount;

    private BigDecimal premiumRate;

    private BigDecimal premiumAmount;

    private BigDecimal prorataPremiumAmount;

    private String premiumRateType;

    private String premiumRateDescription;

    private String sectionType;

    private BigDecimal multiplierRate;

    private BigDecimal multiplierDivisionFactor;

    private BigDecimal annualPremiumAmount;

    private BigDecimal premiumRateDivisionFactor;

    private String compute;

    private BigDecimal limitPeriod;

    private String dualBasis;

    private BigDecimal indemFstPeriod;

    private BigDecimal indemFstPeriodPercentage;

    private BigDecimal indemPeriod;

    private BigDecimal indemRemPeriodPercentage;

    private BigDecimal tolLocRate;

    private BigDecimal tolLocDivisionFactor;

    private BigDecimal emlPercentage;

    private BigDecimal minimumPremiumAmount;

    private BenefitType benefitType;

    private String sectionMandatory;
}
