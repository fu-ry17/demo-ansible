/**
 * 2018-07-19
 */
package com.turnkey.turnquest.gis.quotation.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.turnkey.turnquest.gis.quotation.enums.YesNo;
import com.turnkey.turnquest.gis.quotation.model.audit.BaseAudit;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

/**
 * @author Paul Gichure
 */
@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "GIN_QUOT_PRODUCTS",indexes = {@Index(name = "QP_PRO_INDEX",columnList = "QP_PRO_CODE"),
                                            @Index(name = "QP_PANEL_INDEX",columnList = "QP_AGN_PANEL_ID")})
@JsonIgnoreProperties(value = {"quotationProducts", "quotationProduct","quotations","quotation"},ignoreUnknown = true)
public class QuotationProduct extends BaseAudit implements Serializable {


    private static final long serialVersionUID = 6083290673092468635L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gin-quot-products-seq")
    @SequenceGenerator(name="gin-quot-products-seq", sequenceName = "GIN_QUOT_PRODUCTS_SEQ", allocationSize = 1)
    private Long id;

    @Column(name = "QP_CODE")
    private String code;

    @Column(name = "QP_FK_ID")
    private Long foreignId;

    @Column(name = "QP_PRO_CODE")
    private Long productId;

    @Column(name = "QP_AGN_CODE")
    private Long agentId;

    @Column(name = "QP_AGN_PANEL_ID")
    private Long panelId;

    @Column(name = "QP_PRO_GROUP_ID")
    private Long productGroupId;

    @Column(name = "QP_PRO_SHT_DESC")
    private String productShortDescription;

    @Column(name = "QP_COVER_FROM_DATE")
    private Long policyCoverFrom;

    @Column(name = "QP_COVER_TO_DATE")
    private Long policyCoverTo;

    @Column(name = "QP_WEF_DATE")
    private Long withEffectFromDate;

    @Column(name = "QP_WET_DATE")
    private Long withEffectToDate;

    @Column(name = "QP_QUOT_CODE")
    private Long quotationId;

    @Column(name = "QP_QUOT_NO")
    private String quotationNo;

    @Column(name = "QP_CUR_SYMBOL")
    private String currencySymbol;

    @Column(name = "QP_QUOT_REVISION_NO")
    private String quotationRevisionNumber;

    @Column(name = "QP_TOTAL_SI")
    private BigDecimal totalSumInsured = BigDecimal.ZERO;

    @Column(name = "QP_PREMIUM")
    private BigDecimal totalPremium = BigDecimal.ZERO;

    @Column(name = "QP_TOTAL_GP")
    private BigDecimal basicPremium = BigDecimal.ZERO;

    @Column(name = "QP_REMARKS")
    private String remarks;

    @Column(name = "QP_FAP")
    private BigDecimal futureAnnualPremium = BigDecimal.ZERO;

    @Column(name = "QP_STATUS")
    private String status;

    @Column(name = "QP_BINDER")
    private String fromBinder;

    @Column(name = "QP_BIND_CODE")
    private Long binderId;

    @Column(name = "QP_AGNT_SHT_DESC")
    private String agentCode;

    @Column(name = "QP_CONVERTED")
    private String converted = "Y";

    @Column(name = "QP_MKTR_COM_AMT")
    private BigDecimal marketerCommissionAmount;

    @Column(name = "QP_COMM")
    private BigDecimal commissionAmount = BigDecimal.ZERO;

    @Column(name = "QP_COMM_WHTX")
    private BigDecimal withHoldingTax = BigDecimal.ZERO;

    @Column(name = "QP_SUB_COMM_AMT")
    private BigDecimal subAgentCommissionAmount;

    @Column(name = "QP_LTA_COMM_AMT")
    private BigDecimal longTermArrangementCommissionAmount;

    @Column(name = "QP_QUICK_QUOTE")
    private String isQuickQuote;

    @Column(name = "QP_INSTAL_ALLOWED")
    @Enumerated(EnumType.STRING)
    private YesNo installmentAllowed = YesNo.N;

    ///Partial Premiums
    @Column(name = "QP_FREQ_CALC")
    private String frequencyCalculation;

    @Column(name = "QP_FREQ_OF_PAYMENT")
    private String paymentFrequency;

    @Column(name = "QP_PRIN_ID")
    private Long productInstallmentId;

    @Column(name = "QP_PAID_INSTLMT_NO")
    private Long paidInstallmentNo = 0L;

    @Column(name = "QP_PAID_INSTLMT_AMT")
    private BigDecimal paidInstallmentAmount = BigDecimal.ZERO;

    @Column(name = "QP_INSTLMT_AMT")
    private BigDecimal installmentAmount = BigDecimal.ZERO; //payable premium instalment after first installment

    @Column(name = "QP_INSTLMT_PREM")
    private BigDecimal installmentPremium = BigDecimal.ZERO; //next payable premium installment

    @Column(name = "QP_OS_COM_INSTLMT")
    private BigDecimal outstandingCommission = BigDecimal.ZERO;

    @Column(name = "QP_PAID_COM_INSTLMKT_AMT")
    private BigDecimal paidInstallmentComm = BigDecimal.ZERO;

    @Column(name = "QP_COM_INSTLMT_AMT")
    private BigDecimal commInstallmentAmount = BigDecimal.ZERO; //payable premium instalment after first installment

    @Column(name = "QP_COM_INSTLMT_PREM")
    private BigDecimal commInstallmentPremium = BigDecimal.ZERO; //next payable premium installment

    @Column(name = "QP_OS_PREM_BAL_AMT")
    private BigDecimal outstandingInstallmentAmount = BigDecimal.ZERO;

    @Column(name = "QP_OS_INSTLMT_NO")
    private Long nextInstallmentNo = 1L;

    @Column(name = "QP_PAID_TO_DATE")
    private Long paidToDate; // “Cover to” date for the paid instalment

    @Column(name = "QP_TOT_INSTLMT")
    private Long totalNoOfInstallments = 0L;

    @Column(name = "QP_MATURITY_DATE")
    private Long maturityDate;  // Next Installment Date

    @Column(name = "QP_PYMT_INSTALL_PCTS")
    private Long installmentPlan;

    @Column(name = "QP_REN_NOTIFICATION_DT")
    private Date renewalNotificationDate;

    @Column(name = "QUOT_LAST_VAL_DATE")
    private Long lastValuationDate;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "QP_QUOT_CODE", referencedColumnName = "QUOT_CODE", insertable = false, updatable = false)
    private Quotation quotation;

    @JsonManagedReference
    @OneToMany(mappedBy = "quotationProduct", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private List<QuotationRisk> quotationRisks;

    @JsonManagedReference
    @OneToMany(mappedBy = "quotationProduct", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    private List<QuotationProductTax> quotationProductTaxes;
}
