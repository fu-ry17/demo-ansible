/**
 * 2018-06-26
 */
package com.turnkey.turnquest.gis.quotation.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.turnkey.turnquest.gis.quotation.dto.crm.AgencyDto;
import com.turnkey.turnquest.gis.quotation.dto.crm.ClientDto;
import com.turnkey.turnquest.gis.quotation.dto.crm.CurrencyDto;
import com.turnkey.turnquest.gis.quotation.dto.crm.OrganizationDto;
import com.turnkey.turnquest.gis.quotation.dto.deals.DealDto;
import com.turnkey.turnquest.gis.quotation.enums.PartialType;
import com.turnkey.turnquest.gis.quotation.enums.PolicyInterfaceType;
import com.turnkey.turnquest.gis.quotation.enums.YesNo;
import com.turnkey.turnquest.gis.quotation.model.audit.BaseAudit;
import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

/**
 * @author Paul Gichure
 */
@Data
@RequiredArgsConstructor
@Entity
@Table(name = "GIN_QUOTATIONS", indexes = {@Index(name = "QUOT_ORG_INDEX", columnList = "QUOT_ORG_CODE"),
        @Index(name = "QUOT_CLIENT_INDEX", columnList = "QUOT_PRP_CODE"),
        @Index(name = "QUOT_PANEL_INDEX", columnList = "QUOT_AGN_PANEL_ID")})
public class Quotation extends BaseAudit implements Serializable {

    private static final long serialVersionUID = 6149005517259589599L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gin-quotations-seq")
    @SequenceGenerator(name = "gin-quotations-seq", sequenceName = "GIN_QUOTATIONS_SEQ", allocationSize = 1)
    @Column(name = "QUOT_CODE", nullable = false)
    private Long id;

    @Column(name = "QUOT_REN_BATCH_NO")
    private Long renewalBatchNo;

    @Column(name = "QUOT_NO", nullable = false)
    private String quotationNo;

    @Column(name = "QUOT_PRP_CODE")
    private Long clientId;

    @Column(name = "QUOT_AGNT_AGENT_CODE")
    private Long agencyId;

    @Column(name = "QUOT_INS_ORG_ID")
    private Long insurerOrgId;

    @Column(name = "QUOT_AGN_PANEL_ID")
    private Long panelId;

    @Column(name = "QUOT_BRN_CODE")
    private Long branchId;

    @Column(name = "QUOT_CUR_CODE")
    private Long currencyId;

    @Column(name = "QUOT_COVER_TO", nullable = false)
    private Long coverToDate;

    @Column(name = "QUOT_COVER_FROM", nullable = false)
    private Long coverFromDate;

    @Column(name = "QUOT_TOT_PROPERTY_VAL")
    private BigDecimal totalSumInsured;

    @Column(name = "QUOT_COMMENTS")
    private String comments;

    @Column(name = "QUOT_CURRENT_STATUS")
    private String currentStatus = "D";

    @Column(name = "QUOT_STATUS")
    private String status = "NB";

    @Column(name = "QUOT_GROSS_PREMIUM")
    private BigDecimal grossPremium = BigDecimal.ZERO;

    @Column(name = "QUOT_PREMIUM")
    private BigDecimal premium = BigDecimal.ZERO;

    @Column(name = "QUOT_BASIC_PREMIUM")
    private BigDecimal basicPremium = BigDecimal.ZERO;

    @Column(name = "QUOT_INSTALL_PREMIUM")
    private BigDecimal installmentPremium;

    @Column(name = "QUOT_INSTALL_COMM")
    private BigDecimal installmentCommission;

    @Column(name = "QUOT_COMM_AMT")
    private BigDecimal commissionAmount = BigDecimal.ZERO;

    @Column(name = "QUOT_PREPARED_DT")
    private Long productionDate;

    @Column(name = "QUOT_FREQ_OF_PAYMENT")
    private String paymentFrequency = "A";

    @Column(name = "QUOT_CUR_RATE")
    private BigDecimal currencyRate;

    @Column(name = "QUOT_CUR_SYMBOL")
    private String currencySymbol;

    @Column(name = "QUOT_WEB_POL_ID")
    private Long webPolicyId;

    @Column(name = "QUOT_POL_NO")
    private String policyNo;

    @Column(name = "QUOT_POL_ID")
    private Long policyId;

    @Column(name = "QUOT_PIP_CODE")
    private Long insuredCode;

    @Column(name = "QUOT_ORG_CODE")
    private Long organizationId;

    @Column(name = "QUOT_WHTAX")
    private BigDecimal withholdingTax;

    @Column(name = "QUOT_PAYREF")
    private String paymentRef;

    @Column(name = "QUOT_IS_READ")
    private boolean readStatus = false;

    @Column(name = "QUOT_IS_RENEWAL")
    private boolean renewal = false;

    @Column(name = "QUOT_UNDERWRITING_YEAR")
    private Long underwritingYear= (long) Calendar.getInstance().get(Calendar.YEAR);

    @Column(name = "QUOT_PROPOSAL_CONSENT")
    private Boolean isProposalConsented = false;

    @Column(name = "QUOT_LAST_VAL_DATE")
    private Long lastValuationDate;

    @OneToMany(mappedBy = "quotation", cascade = CascadeType.REMOVE)
    @JsonManagedReference
    @LazyCollection(LazyCollectionOption.FALSE)
    @ToString.Exclude
    private List<QuotationProduct> quotationProducts;

    @Transient
    @JsonIgnoreProperties({"organization"})
    private transient AgencyDto agent;

    @Transient
    @JsonIgnore
    private transient CurrencyDto currency;

    @Transient
    private transient ClientDto client;

    @Transient
    private transient OrganizationDto organization;

    @Transient
    private transient DealDto deal;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Quotation quotation = (Quotation) o;
        return id != null && Objects.equals(id, quotation.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
