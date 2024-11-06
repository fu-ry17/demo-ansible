package com.turnkey.turnquest.gis.quotation.dto.computation;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Premium {
    private Rate rate;
    private BigDecimal sectionLimit;
    private BigDecimal usedLimit;
    private BigDecimal sumInsured;
    private BigDecimal premiumAmt;

}
