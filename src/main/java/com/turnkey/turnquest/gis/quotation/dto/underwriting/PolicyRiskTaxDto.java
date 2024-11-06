package com.turnkey.turnquest.gis.quotation.dto.underwriting;

import com.turnkey.turnquest.gis.quotation.enums.TaxRateCategory;
import com.turnkey.turnquest.gis.quotation.enums.TaxRateInstallmentType;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PolicyRiskTaxDto {
    private Long id;

    private Long policyRiskId;

    private String transactionTypeCode;

    private String policyNumber;

    private BigDecimal amount;

    private BigDecimal rate;

    private Long taxRateId;

    private Long transactionTypeSubClassId;

    private String policyRenewalEndorsementNumber;

    private String tlLevelCode;

    private String rateType;

    private String rateDescription;

    private BigDecimal endorsementDiffAmount;

    private String taxType;

    private String riskOrPolicyLevel;

    private BigDecimal coInsuranceAmount;

    private BigDecimal coInsuranceEndorsementDifferenceAmount;

    private String applicationArea;

    private TaxRateInstallmentType taxRateInstallmentType;

    private TaxRateCategory taxRateCategory;

    private String calculationMode;

    private Long productSubClassId;

    private BigDecimal divisionFactor;
}
