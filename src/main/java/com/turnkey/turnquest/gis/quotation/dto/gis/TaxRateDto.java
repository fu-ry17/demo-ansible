package com.turnkey.turnquest.gis.quotation.dto.gis;

import com.turnkey.turnquest.gis.quotation.enums.TaxRateCategory;
import com.turnkey.turnquest.gis.quotation.enums.TaxRateInstallmentType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class TaxRateDto implements Serializable {

    private static final long serialVersionUID = -8052434587485521093L;

    private Long id;

    private String code;

    private BigDecimal amount;

    private BigDecimal rate;

    private Long withEffectFromDate;

    private Long withEffectToDate;

    private Long subClassId;

    private Long rangeFrom;

    private Long rangeTo;

    private String calculationMode;

    private BigDecimal minAmt;

    private BigDecimal roundNxt;

    private String transactionTypeCode;

    private String tlLvlCode;

    private String rateDesc;

    private BigDecimal divisionFactor;

    private String rateType;

    private String applicationLevel;

    private String riskPolLevel;

    private String taxType;

    private Long productSubClassId;

    private Long orgCode;

    private String applicationArea;

    private String multiplierApplicable;

    @Enumerated(EnumType.STRING)
    private TaxRateCategory category;

    @Enumerated(EnumType.STRING)
    private TaxRateInstallmentType installmentType;

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof TaxRateDto)) {
            return false;
        }
        TaxRateDto other = (TaxRateDto) obj;

        if (other.id == null) {
            return false;
        }
        return other.transactionTypeCode.equalsIgnoreCase(this.transactionTypeCode)
                || other.id.equals(this.id);

    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
