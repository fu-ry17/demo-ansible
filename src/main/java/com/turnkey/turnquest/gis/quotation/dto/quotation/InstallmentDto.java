package com.turnkey.turnquest.gis.quotation.dto.quotation;

import com.turnkey.turnquest.gis.quotation.dto.DistributionTaxDto;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class InstallmentDto {

    private Long productId;

    private BigDecimal basicPremium;

    private BigDecimal totalCommission;

    private BigDecimal staticTaxes;

    private List<DistributionTaxDto> distributionTaxes;

    private Long installmentPlanId;

    private Long instalmentNo;

    private Long maturityDate;
}
