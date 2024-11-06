package com.turnkey.turnquest.gis.quotation.dto.computation;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class Rate {
    private Long id;
    private BigDecimal rate;
    private Long sectionId;
    private BigDecimal subClassId;
    private Long binderId;
    private String sectionMandatory;
    private BigDecimal divisionFactor;
    private BigDecimal multiplierRate;
    private BigDecimal multiplierDivisionFactor;
    private BigDecimal minimumRate;
    private BigDecimal maximumRate;
    private BigDecimal freeLimit;
    private String rateDescription;
    private String ncdApplicable;
    private String excessProtectorApplicable;
    private String proratedFull;
    private BigDecimal premiumMinimumAmount;
    private BigDecimal rangeFrom;
    private BigDecimal rangeTo;
    private Long productSubclassId;
    private Long subClassSectionId;
    private Long withEffectFromDate;
    private Long withEffectToDate;
    private Binder binder;
    private CoverType coverType;
    private Currency currency;
    private SubClassCoverTypeSection subClassCoverTypeSection;
    private List<CommissionRate> commRates;
    private BigDecimal from;
    private String rateDesc;
    private String rateType;
    private String prorataType;
    private BigDecimal computedMinimumAmt;
    private BigDecimal to;
    private String applyBenefit;
    private String applyExcessProtector;
    private String ncd;
    private BigDecimal multiplier;
    private Long order;
    private Long group;
    private String applicationArea;
    private String category;
    private String transactionTypeCode;
    private String taxType;

}
