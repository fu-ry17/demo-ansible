package com.turnkey.turnquest.gis.quotation.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.turnkey.turnquest.gis.quotation.model.audit.BaseAudit;
import com.turnkey.turnquest.gis.quotation.model.id.QuotationRiskClauseId;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "GIN_QUOT_RISK_CLAUSES")
@IdClass(QuotationRiskClauseId.class)
public class QuotationRiskClause extends BaseAudit implements Serializable {

    private static final long serialVersionUID = 1845834806988774416L;
    @Id
    @Column(name = "QRC_CLS_CODE")
    private Long clauseId;

    @Column(name = "QRC_CLS_SHT_DESC")
    private String clauseShortDescription;

    @Column(name = "QRC_QUOT_CODE")
    private Long quotationId;

    @Id
    @Column(name = "QRC_QR_CODE")
    private Long quotationRiskId;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "QRC_QR_CODE", insertable = false, updatable = false)
    private QuotationRisk quotationRisk;

    @Column(name = "QRC_CLAUSE")
    private String clauseWording;

    @Column(name = "QRC_CLS_EDITABLE")
    private String clauseEditable;

    @Column(name = "QRC_CLS_TYPE")
    private String clauseType;

    @Column(name = "QRC_CLS_HEADING")
    private String clauseHeading;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        QuotationRiskClause that = (QuotationRiskClause) o;
        return clauseId != null && Objects.equals(clauseId, that.clauseId)
                && quotationRiskId != null && Objects.equals(quotationRiskId, that.quotationRiskId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clauseId, quotationRiskId);
    }
}
