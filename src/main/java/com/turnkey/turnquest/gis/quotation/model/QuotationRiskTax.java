package com.turnkey.turnquest.gis.quotation.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.turnkey.turnquest.gis.quotation.dto.gis.TaxRateDto;
import com.turnkey.turnquest.gis.quotation.enums.TaxRateCategory;
import com.turnkey.turnquest.gis.quotation.enums.TaxRateInstallmentType;
import com.turnkey.turnquest.gis.quotation.model.audit.BaseAudit;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "GIN_QUOT_RISK_TAXES")
@JsonIgnoreProperties(value = {"quotationRisk", "quotationRisks", "quotationRiskTaxes", "quotationRiskTax"}, ignoreUnknown = true)
public class QuotationRiskTax extends BaseAudit implements Serializable {
    private static final long serialVersionUID = -4991709880604274964L;

    @Id
    @Column(name = "QRT_CODE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "quot-risk-tax-seq")
    @SequenceGenerator(name = "quot-risk-tax-seq", sequenceName = "gin_quot_risk_taxes_seq", allocationSize = 1)
    private Long id;

    @Column(name = "QRT_QR_CODE")
    private Long quotationRiskId;

    @Column(name = "QRT_TAXR_CODE")
    private Long taxRateId;

    @Column(name = "QRT_RATE")
    private BigDecimal rate;

    @Column(name = "QRT_DIV_FACTOR")
    private BigDecimal divisionFactor;

    @Column(name = "QRT_TAX_AMT")
    private BigDecimal taxAmount = BigDecimal.ZERO;

    @Column(name = "QRT_TRAC_TRNT_CODE")
    private String transactionTypeCode;

    @Column(name = "QRT_RATE_TYPE")
    private String taxRateType;

    @Column(name = "QRT_TAX_TYPE")
    private String taxType;

    @Column(name = "QRT_RATE_DESC")
    private String taxRateDescription;

    @Column(name = "QRT_TRNT_RENEWAL_ENDOS")
    private String renewalEndorsement;

    @Column(name = "QRT_TL_LVL_CODE")
    private String tlLvlCode;

    @Column(name = "QRT_RISK_PROD_LEVEL")
    private String riskOrProductLevel;

    @Column(name = "QRT_PROD_SUBCLASS")
    private Long productSubClassId;

    @Column(name = "QRT_APPLICATION_AREA")
    private String applicationArea;

    @Column(name = "QRT_TAX_CAL_MODE")
    private String calculationMode;

    @Enumerated(EnumType.STRING)
    @Column(name = "QRT_RATE_CATEGORY")
    private TaxRateCategory taxRateCategory;

    @Enumerated(EnumType.STRING)
    @Column(name = "QRT_INSTAL_APPL_TYPE")
    private TaxRateInstallmentType taxRateInstallmentType;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "QRT_QR_CODE", referencedColumnName = "id", updatable = false, insertable = false)
    private QuotationRisk quotationRisk;

    @Transient
    private TaxRateDto taxRate;


}
