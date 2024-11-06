package com.turnkey.turnquest.gis.quotation.dto.computation;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Commission {
    private BigDecimal commission;
    private Rate rate;
}
