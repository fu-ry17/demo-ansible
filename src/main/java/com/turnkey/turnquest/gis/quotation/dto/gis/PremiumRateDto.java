package com.turnkey.turnquest.gis.quotation.dto.gis;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class PremiumRateDto implements Serializable {

    private static final long serialVersionUID = -135453090507892448L;
    private Long id;

    private String code;

    private Long sectionId;

    private Long binderId;

    private String sectionShortDescription;

    private BigDecimal rate;

    private Long rangeFrom;

    private Long rangeTo;

    private Long withEffectFromDate;

    private Long withEffectToDate;

    private Long subClassId;

    private String rateDescription;

    private String type;

    private BigDecimal divisionFactor;

    private BigDecimal multiplierDivisionFactor;

    private BigDecimal multiplierRate;

    private BigDecimal premiumMinimumAmount;

    private BigDecimal maximumRate;

    private BigDecimal minimumRate;

    private String proratedFull;

    private String excessProtectorApplicable;

    private String ncdApplicable;

    private Long territoryId;

    private Long endorsementMinAmt;

    private Long ncdLevel;

    private String sectionType;

    private BigDecimal freeLimit;

    private String sumInsuredLimit;

    private String sumInsuredLimitType;

    private Long sumInsuredRate;

    private Long groupCode;

    private Long currencyCode;

    private String rateFrequencyType;

    private String sectionMandatory;

    private Long productSubclassId;

    private Long subClassSectionId;
}
