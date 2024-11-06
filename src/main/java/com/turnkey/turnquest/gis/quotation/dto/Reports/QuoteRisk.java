package com.turnkey.turnquest.gis.quotation.dto.Reports;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class QuoteRisk {

    private String riskId;

    private Long withEffectFromDate;

    private Long withEffectToDate;

    //Generated from withEffectToDate
    private String renewalDate;

    private Long coverTypeId;

    private String coverTypeCode;

    private Long subClassId;

    private Long productSubClassId;

    private BigDecimal totalPremium;

    private BigDecimal basicPremium;

    private BigDecimal annualPremium;

    private BigDecimal futureAnnualPremium;

    private BigDecimal value;

    private String valueString;

    private String premiumString;

    private String coverTypeWording;

    private Long valuerOrgId;

    private QuoteValuerInfo quotationValuationInfo;

}
