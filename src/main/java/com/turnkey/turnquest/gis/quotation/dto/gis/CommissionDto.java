package com.turnkey.turnquest.gis.quotation.dto.gis;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Date;

@Data
public class CommissionDto {
    private Long id;

    private String transactionTypeCode;

    private BigDecimal rate;

    private Date withEffectFromDate;

    private Date withEffectToDate;

    private Long subClassId;

    private Long accountTypeId;

    private String tlLvlCode;

    private String rateDescription;

    private BigDecimal divisionFactor;

    private String rateType;

    private BigDecimal rangeFrom;

    private BigDecimal rangeTo;

    private String applicationLevel;

    private Long binderId;

    private String agentTypeShortDescription;

    private BigDecimal retainerRate;

    private String retainerDescription;

    private BigDecimal termFrom;

    private BigDecimal termTo;
}
