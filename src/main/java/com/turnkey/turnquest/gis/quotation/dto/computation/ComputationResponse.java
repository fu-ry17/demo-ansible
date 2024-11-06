package com.turnkey.turnquest.gis.quotation.dto.computation;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ComputationResponse {
    private int totalPremium;
    private List<Premium> premiums;
    private List<Tax> taxes;
    private List<Commission> commissions;
    private String propertyId;
    private Long coverFromDate;
    private Long coverToDate;
    private BigDecimal sumInsured;
}