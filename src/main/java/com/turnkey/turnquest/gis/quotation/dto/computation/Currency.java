package com.turnkey.turnquest.gis.quotation.dto.computation;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Currency {
    private BigDecimal id;
    private String name;
    private String symbol;
    private String roundingOff;
    private BigDecimal rounding;
    private String iso3;
    private BigDecimal code;

}
