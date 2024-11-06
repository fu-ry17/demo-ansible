package com.turnkey.turnquest.gis.quotation.dto.computation;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Tax {
    private BigDecimal tax;
    private Rate rate;
}
