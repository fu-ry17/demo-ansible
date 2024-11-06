package com.turnkey.turnquest.gis.quotation.dto.computation;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CoverType {
    private String shtDesc;
    private BigDecimal code;
    private String description;
    private String details;
    private String name;
    private BigDecimal id;

}
