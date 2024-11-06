package com.turnkey.turnquest.gis.quotation.dto.quotation;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdateInstallmentDto {

    private Long quotationProductId;

    private BigDecimal newCommissionInstallmentAmount;

    private BigDecimal newInstallmentAmount;

}
