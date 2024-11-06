package com.turnkey.turnquest.gis.quotation.dto.gis;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaxRateFilterDto {
    private Long organizationId;

    private Long productId;

    private String applicationLevel;

    private String transactionTypeCode;
}
