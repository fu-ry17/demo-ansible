package com.turnkey.turnquest.gis.quotation.dto.gis;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class SectionShortPeriodRateDto {
    private Long id;

    private BigDecimal annualPremiumRate;

    private BigDecimal periodFrom;

    private BigDecimal periodNotExceeding;

    private BigDecimal rateDivisionFactor;

    private Long subClassId;

    private Long sectionId;

    private Date withEffectFromDate;

    private Date withEffectToDate;
}
