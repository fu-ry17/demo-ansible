package com.turnkey.turnquest.gis.quotation.dto.computation;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Section {
    private String sectType;
    private String shtDesc;
    private BigDecimal code;
    private String description;
    private String name;
    private BigDecimal id;

}
