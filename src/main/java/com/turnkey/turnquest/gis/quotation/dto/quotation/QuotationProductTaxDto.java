package com.turnkey.turnquest.gis.quotation.dto.quotation;

import com.turnkey.turnquest.gis.quotation.enums.TaxRateCategory;
import com.turnkey.turnquest.gis.quotation.enums.TaxRateInstallmentType;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class QuotationProductTaxDto {
    private Long id;

    private Long quotationProductId;

    private Long taxRateId;

    private BigDecimal rate;

    private BigDecimal divisionFactor;

    private BigDecimal taxAmount = BigDecimal.ZERO;

    private String transactionTypeCode;

    private String taxRateType;

    private String taxType;

    private String taxRateDescription;

    private String renewalEndorsement;

    private String tlLvlCode;

    private String riskOrProductLevel;

    private Long productSubClassId;

    private String applicationArea;

    private String calculationMode;

    private TaxRateCategory taxRateCategory;

    private TaxRateInstallmentType taxRateInstallmentType;

}
