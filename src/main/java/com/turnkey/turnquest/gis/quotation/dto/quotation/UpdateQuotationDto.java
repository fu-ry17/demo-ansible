package com.turnkey.turnquest.gis.quotation.dto.quotation;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdateQuotationDto {
    private Long coverFromDate;

    private Long coverToDate;

    private Long quotationProductId;

    private Long quotationId;

    private BigDecimal sumInsured;
}
