package com.turnkey.turnquest.gis.quotation.dto.crm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyRateRequestDto {
    private Long organizationId;

    private Long fromCurrencyId;
}
