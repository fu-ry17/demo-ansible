package com.turnkey.turnquest.gis.quotation.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.turnkey.turnquest.gis.quotation.dto.gis.TaxRateDto;
import com.turnkey.turnquest.gis.quotation.enums.TaxRateCategory;
import com.turnkey.turnquest.gis.quotation.enums.TaxRateInstallmentType;
import com.turnkey.turnquest.gis.quotation.model.audit.BaseAudit;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "GIN_QUOT_PRODUCT_TAXES")
@JsonIgnoreProperties(value = {"quotationProduct","quotationProducts","quotationProductTaxes", "quotationProductTax"},ignoreUnknown = true)
public class QuotationProductTax extends BaseAudit implements Serializable {
    private static final long serialVersionUID = -4991709880604274964L;

    @Id
    @Column(name = "QPT_CODE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gin-quot-products-tax-seq")
    @SequenceGenerator(name="gin-quot-products-tax-seq", sequenceName = "GIN_QUOT_PRODUCT_TAXES_SEQ", allocationSize = 1)
    private Long id;

    @Column(name = "QPT_QP_CODE")
    private Long quotationProductId;

    @JsonBackReference
    @ManyToOne()
    @JoinColumn(name = "QPT_QP_CODE", referencedColumnName = "id", updatable = false, insertable = false)
    private QuotationProduct quotationProduct;

    @Column(name = "QPT_TAXR_CODE")
    private Long taxRateId;

    @Column(name = "QPT_RATE")
    private BigDecimal rate;

    @Column(name = "QPT_DIV_FACTOR")
    private BigDecimal divisionFactor;

    @Column(name = "QPT_TAX_AMT")
    private BigDecimal taxAmount = BigDecimal.ZERO;

    @Column(name = "QPT_TRAC_TRNT_CODE")
    private String transactionTypeCode;

    @Column(name = "QPT_RATE_TYPE")
    private String taxRateType;

    @Column(name = "QPT_TAX_TYPE")
    private String taxType;

    @Column(name = "QPT_RATE_DESC")
    private String taxRateDescription;

    @Column(name = "QPT_TRNT_RENEWAL_ENDOS")
    private String renewalEndorsement;

    @Column(name = "QPT_TL_LVL_CODE")
    private String tlLvlCode;

    @Column(name = "QPT_RISK_PROD_LEVEL")
    private String riskOrProductLevel;

    @Column(name = "QPT_PROD_SUBCLASS")
    private Long productSubClassId;

    @Column(name = "QPT_APPLICATION_AREA")
    private String applicationArea;

    @Column(name = "QPT_TAX_CAL_MODE")
    private String calculationMode;

    @Enumerated(EnumType.STRING)
    @Column(name = "QPT_RATE_CATEGORY")
    private TaxRateCategory taxRateCategory;

    @Enumerated(EnumType.STRING)
    @Column(name = "QPT_INSTAL_APPL_TYPE")
    private TaxRateInstallmentType taxRateInstallmentType;

    @Transient
    private TaxRateDto taxRate;


}
