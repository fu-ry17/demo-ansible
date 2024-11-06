package com.turnkey.turnquest.gis.quotation.dto.underwriting;

import com.turnkey.turnquest.gis.quotation.enums.TaxRateCategory;
import com.turnkey.turnquest.gis.quotation.enums.TaxRateInstallmentType;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class PolicyTaxDto implements Serializable {

    private Long id;

    private Long policyId;

    private String transactionTypeCode;

    private String policyNumber;

    private BigDecimal amount;

    private BigDecimal rate;

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

    private TaxRateCategory taxRateCategory;

    private String calculationMode;

    private TaxRateInstallmentType installmentType;

    private Long productSubClassId;

    private BigDecimal divisionFactor;
}
