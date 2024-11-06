package com.turnkey.turnquest.gis.quotation.dto.computation;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SubClassSection {
    private BigDecimal id;
    private String code;
    private BigDecimal subClassCode;
    private BigDecimal sectionCode;
    private String sectionShortDesc;
    private String sectionType;
    private String declaration;
    private BigDecimal productSubClassId;
    private BigDecimal limitAmount;
    private String sectType;
    private String shtDesc;
    private String name;
}
