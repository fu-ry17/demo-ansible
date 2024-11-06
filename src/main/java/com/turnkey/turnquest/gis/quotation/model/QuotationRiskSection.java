/**
 * 2018-07-19
 */
package com.turnkey.turnquest.gis.quotation.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.turnkey.turnquest.gis.quotation.dto.gis.SectionDto;
import com.turnkey.turnquest.gis.quotation.enums.BenefitType;
import com.turnkey.turnquest.gis.quotation.enums.PremiumRateType;
import com.turnkey.turnquest.gis.quotation.model.audit.BaseAudit;
import lombok.*;
import org.hibernate.Hibernate;

import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * @author Paul Gichure
 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "gin_quot_risk_limits")
@JsonIgnoreProperties(value = {"quotationRiskSections","quotationRiskSection","quotationRisk","quotationRisks"},ignoreUnknown = true)
public class QuotationRiskSection extends BaseAudit implements Serializable {

    private static final long serialVersionUID = -1104262096851226478L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "quot-risk-limits-seq")
    @SequenceGenerator(name="quot-risk-limits-seq", sequenceName = "gin_quot_risk_limits_seq", allocationSize = 1)
    @Column(name = "QRL_CODE")
    private Long id;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "QRL_QR_CODE", referencedColumnName = "id", insertable = false, updatable = false)
    private QuotationRisk quotationRisk;

    @Column(name = "QRL_DESC")
    private String description;

    @Column(name = "QRL_FK_ID")
    private Long foreignId;

    @Column(name = "QRL_SECT_CODE")
    private Long sectionId;

    @Column(name = "QRL_SECT_SHT_DESC")
    private String sectionCode;

    @Column(name = "QRL_SEC_CODE")
    private Long subClassSectionId;

    @Column(name = "QRL_SEC_SHT_DESC")
    private String subClassSectionDesc;

    @Column(name = "QRL_SECT_TYPE")
    private String sectionType;

    @Column(name = "QRL_QR_CODE")
    private Long quotationRiskId;

    @Column(name = "QRL_LIMIT_AMT")
    private BigDecimal limitAmount;

    @Column(name = "QRL_ANNUAL_PREM")
    private BigDecimal annualPremiumAmount;

    @Column(name = "QRL_PREM_AMT")
    private BigDecimal premiumAmount = BigDecimal.ZERO;

    @Column(name = "QRL_MIN_PREMIUM")
    private BigDecimal minimumPremiumAmount;

    @Column(name = "QRL_USED_LIMIT")
    private BigDecimal usedLimitAmount;

    @Column(name = "QRL_FREE_LIMIT")
    private BigDecimal freeLimitAmount = BigDecimal.ZERO;

    @Column(name = "QRL_PREM_RATE")
    private BigDecimal premiumRate;

    @Column(name = "QRL_MULTIPLIER_RATE")
    private BigDecimal multiplierRate = BigDecimal.ONE;

    @Column(name = "QRL_MULTIPLIER_DIV_FACTOR")
    private BigDecimal multiplierDivisionFactor = BigDecimal.ONE;

    @Column(name = "QRL_RATE_DIV_FACTOR")
    private BigDecimal rateDivisionFactor = BigDecimal.valueOf(100);

    @Enumerated(EnumType.STRING)
    @Column(name = "QRL_RATE_TYPE")
    private PremiumRateType rateType = PremiumRateType.FXD;

    @Column(name = "QRL_PRORATED")
    private String prorated = "P";

    @Column(name = "QRL_RATE_DESC")
    private String premiumRateDescription= "Percent";

    @Column(name = "QRL_CALC_GROUP")
    private BigDecimal calculationGroup;

    @Column(name = "QRL_ROW_NUM")
    private BigDecimal rowNumber;

    @Column(name = "QRL_COMPUTE")
    private String compute;

    @Column(name = "QRL_BENEFIT_TYPE")
    private BenefitType benefitType;

    @Column(name = "QRL_DUAL_BASIS")
    private String dualBasis;

    @Column(name = "QRL_SI_RATE")
    private BigDecimal sumInsuredRate;

    @Column(name = "QRL_SI_LIMIT_TYPE")
    private String sumInsuredLimitType;

    @Column(name = "QRL_SEC_MANDATORY")
    private String sectionMandatory;

    @Transient
    private SectionDto section;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        QuotationRiskSection that = (QuotationRiskSection) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
