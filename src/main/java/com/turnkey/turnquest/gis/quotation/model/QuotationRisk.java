package com.turnkey.turnquest.gis.quotation.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.turnkey.turnquest.gis.quotation.dto.gis.CoverTypeDto;
import com.turnkey.turnquest.gis.quotation.enums.EndorsementType;
import com.turnkey.turnquest.gis.quotation.enums.ValuationStatus;
import com.turnkey.turnquest.gis.quotation.enums.YesNo;
import com.turnkey.turnquest.gis.quotation.model.audit.BaseAudit;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "GIN_QUOT_RISKS")
@EqualsAndHashCode(of = "id", callSuper = true)
@ToString(exclude = {"quotationRiskSections"})
@JsonIgnoreProperties(value = {"quotationRisk", "quotationRisks", "quotationProduct", "quotationProducts"}, ignoreUnknown = true)
public class QuotationRisk extends BaseAudit implements Serializable {

    private static final long serialVersionUID = 7301565373894650713L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gin-quotation-risks-seq")
    @SequenceGenerator(name = "gin-quotation-risks-seq", sequenceName = "GIN_QUOT_RISKS_SEQ", allocationSize = 1)
    private Long id;

    @Column(name = "QR_CODE")
    private String code;

    @Column(name = "QR_FK_ID")
    private Long foreignId;

    @Column(name = "QR_POL_IPU_ID")
    private Long policyRiskId;

    @Column(name = "QR_QP_CODE")
    private Long quotationProductId;

    @Column(name = "QR_BIND_CODE")
    private Long binderId;

    @Column(name = "QR_PROPERTY_ID")
    private String riskId;

    @Column(name = "QR_ITEM_DESC")
    private String itemDescription;

    @Column(name = "QR_CERT_NO")
    private String certificateNo;

    @Column(name = "QR_QUOT_REVISION_NO")
    private BigDecimal quotationRevisionNumber;

    @Column(name = "QR_CLNT_TYPE")
    private String clientType = "C";

    @Column(name = "QR_QTY")
    private Long quantity;

    @Column(name = "QR_WEF")
    private Long withEffectFromDate;

    @Column(name = "QR_WET")
    private Long withEffectToDate;

    @Column(name = "QR_COVT_ID")
    private Long coverTypeId;

    @Column(name = "QR_COVT_CODE")
    private String coverTypeCode;

    @Column(name = "QR_SCL_CODE")
    private Long subClassId;

    @Column(name = "QR_CLP_CODE")
    private Long productSubClassId;

    @JsonIgnore
    @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean akiVerified;

    @Column(name = "QR_PRP_CODE")
    private Long clientId;

    @Column(name = "QR_PREMIUM")
    private BigDecimal totalPremium = BigDecimal.ZERO;

    @Column(name = "QR_TOT_GP")
    private BigDecimal basicPremium = BigDecimal.ZERO;

    @Column(name = "QR_ANNUAL_PREM")
    private BigDecimal annualPremium;

    @Column(name = "QR_FP")
    private BigDecimal futureAnnualPremium = BigDecimal.ZERO;

    @Column(name = "QR_VALUE")
    private BigDecimal value = BigDecimal.ZERO;

    @Column(name = "QR_MIN_PREMIUM_USED")
    private String minimumPremiumUsed;

    @Column(name = "QR_COVER_DAYS")
    private Long coverDays;

    @Column(name = "QR_NCD_LEVEL")
    private Long ncdLevel;

    @Column(name = "QR_COM_RATE")
    private BigDecimal commissionRate = BigDecimal.ZERO;

    @Column(name = "QR_COMM_AMT")
    private BigDecimal commissionAmount = BigDecimal.ZERO;

    @Column(name = "QR_LTA_COMM_RATE")
    private BigDecimal longTermAgreementCommissionRate = BigDecimal.ZERO;

    @Column(name = "QR_LTA_COMM_AMT")
    private BigDecimal longTermAgreementCommissionAmount = BigDecimal.ZERO;

    @Column(name = "QR_SUB_COMM_RATE")
    private BigDecimal subAgentCommissionRate;

    @Column(name = "QR_SUB_COMM_AMT")
    private BigDecimal subAgentCommissionAmount;

    @Column(name = "QR_ENFORCE_CVT_MIN_PREM")
    private String enforceCoverTypeMinimumPremium;

    @Column(name = "QR_MKTR_COM_AMT")
    private BigDecimal marketerCommissionAmount;

    @Column(name = "QR_MKTR_COM_RATE")
    private BigDecimal marketerCommissionRate;

    @Column(name = "QR_COMM_WHTX")
    private BigDecimal withHoldingTax = BigDecimal.ZERO;

    @Column(name = "QR_VALUATION_STATUS")
    @Enumerated(EnumType.STRING)
    private ValuationStatus valuationStatus = ValuationStatus.OPEN;

    @Column(name = "QR_END_TYPE")
    @Enumerated(EnumType.STRING)
    private EndorsementType endorsementType = EndorsementType.NONE;

    @Column(name = "QR_INSTAL_ALLOWED")
    @Enumerated(EnumType.STRING)
    private YesNo installmentAllowed = YesNo.N;

    @Column(name = "QR_FREQ_CALC")
    private String frequencyCalculation;

    @Column(name = "QR_FREQ_OF_PAYMENT")
    private String paymentFrequency;

    @Column(name = "QR_PRIN_ID")
    private Long productInstallmentId;

    @Column(name = "QR_PAID_INSTLMT_NO")
    private Long paidInstallmentNo;

    @Column(name = "QR_PAID_INSTLMT_AMT")
    private BigDecimal paidInstallmentAmount = BigDecimal.ZERO;

    @Column(name = "QR_INSTLMT_AMT")
    private BigDecimal installmentAmount = BigDecimal.ZERO; //payable premium instalment after first installment

    @Column(name = "QR_INSTLMT_PREM")
    private BigDecimal installmentPremium = BigDecimal.ZERO; //next payable premium installment

    @Column(name = "QR_OS_COM_INSTLMT")
    private BigDecimal outstandingCommission = BigDecimal.ZERO;

    @Column(name = "QR_PAID_COM_INSTLMT_AMT")
    private BigDecimal paidInstallmentComm = BigDecimal.ZERO;

    @Column(name = "QR_COM_INSTLMT_AMT")
    private BigDecimal commInstallmentAmount = BigDecimal.ZERO; //payable premium instalment after first installment

    @Column(name = "QR_COM_INSTLMT_PREM")
    private BigDecimal commInstallmentPremium = BigDecimal.ZERO; //next payable premium installment

    @Column(name = "QR_OS_PREM_BAL_AMT")
    private BigDecimal outstandingInstallmentAmount = BigDecimal.ZERO;

    @Column(name = "QR_OS_INSTLMT_NO")
    private Long nextInstallmentNo = 1L;

    @Column(name = "QR_PAID_TO_DATE")
    private Long paidToDate; // “Cover to” date for the paid instalment

    @Column(name = "QR_TOT_INSTLMT")
    private Long totalNoOfInstallments = 0L;

    @Column(name = "QR_MATURITY_DATE")
    private Long maturityDate;  // Next Installment Date

    @Column(name = "QR_BUT_CHARGE")
    private BigDecimal butCharge;

    @Column(name = "QR_PYMT_INSTALL_PCTS")
    private Long installmentPlan;

    @Column(name = "QR_VALUER_ORG_ID")
    private Long valuerOrgId;

    @Column(name = "QR_VALUER_BRANCH_ID")
    private Long valuerBranchId;

    @Column(name = "QUOT_LAST_VAL_DATE")
    private Long lastValuationDate;

    @JsonManagedReference
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "QR_MPS_ID", referencedColumnName = "MPS_IPU_ID")
    private MotorSchedules motorSchedules;

    @JsonManagedReference
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "QD_QR_ID", referencedColumnName = "id", insertable = false, updatable = false)
    private List<QuoteDocument> quoteDocument;

    @JsonManagedReference
    @OneToMany(mappedBy = "quotationRisk", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<QuotationRiskSection> quotationRiskSections;

    @JsonManagedReference
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "QRT_QR_CODE", referencedColumnName = "id", updatable = false, insertable = false)
    private List<QuotationRiskTax> quotationRiskTaxes = new ArrayList<>();

    @JsonManagedReference
    @OneToMany(mappedBy = "quotationRisk", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<QuotationRiskClause> quotationRiskClauses = new ArrayList<>();

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "QR_QP_CODE", referencedColumnName = "id", insertable = false, updatable = false)
    private QuotationProduct quotationProduct;

    @Transient
    private CoverTypeDto coverType;


}
