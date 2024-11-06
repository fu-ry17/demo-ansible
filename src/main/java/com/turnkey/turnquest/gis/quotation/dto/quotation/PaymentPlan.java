package com.turnkey.turnquest.gis.quotation.dto.quotation;

import com.turnkey.turnquest.gis.quotation.dto.DistributionTaxDto;
import com.turnkey.turnquest.gis.quotation.enums.InstallmentCalculation;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class PaymentPlan {

    private String frequency;

    private InstallmentCalculation frequencyCalculation;

    private List<BigDecimal> installments = new ArrayList<>();

    private String commissionFrequency;

    private List<BigDecimal> commissions = new ArrayList<>();

    private Long planId;

    private Long totalInstallments;

    private BigDecimal staticTaxes;

    private List<DistributionTaxDto> distributionTaxes;

    private List<BigDecimal> basicPremiums = new ArrayList<>();
}