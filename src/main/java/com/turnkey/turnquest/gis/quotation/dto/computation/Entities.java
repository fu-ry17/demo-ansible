package com.turnkey.turnquest.gis.quotation.dto.computation;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Entities {
    private BigDecimal id;
    private String entityCode;
    private String imageUrl;
    private String organizationName;

}
