package com.turnkey.turnquest.gis.quotation.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.turnkey.turnquest.gis.quotation.dto.gis.ClauseDto;
import com.turnkey.turnquest.gis.quotation.model.audit.BaseAudit;
import com.turnkey.turnquest.gis.quotation.model.id.QuotationClauseId;
import lombok.*;
import org.hibernate.Hibernate;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "GIN_QUOT_CLAUSES")
@IdClass(QuotationClauseId.class)
public class QuotationClause extends BaseAudit implements Serializable {

    private static final long serialVersionUID = -3931416716344643455L;

    @Id
    @Column(name = "QC_CLS_CODE")
    private Long clauseId;

    @Id
    @Column(name = "QC_QP_CODE")
    private Long quotationProductId;

    @ManyToOne
    @JsonIgnoreProperties("quotationClauses")
    @JoinColumn(name = "QC_QP_CODE", insertable = false, updatable = false)
    private QuotationProduct quotationProduct;

    @Deprecated
    @Column(name = "QC_QUOT_CODE")
    private Long quotationId;

    @Column(name = "QC_PRO_CODE")
    private Long productId;


    @Column(name = "QC_QUOT_REVISION_NO")
    private Long quotationRevisionNumber;

    @Column(name = "QC_QUOT_NO")
    private String quotationNumber;

    @Column(name = "QC_CLS_HEADING")
    private String clauseHeading;

    @Column(name = "QC_CLAUSE")
    private String clauseWording;

    @Column(name = "QC_CLS_EDITABLE")
    private String clauseEditable;

    @Column(name = "QC_SBCL_SCL_CODE")
    private Long subClassId;

    @Column(name = "QC_CLS_TYPE")
    private String clauseType;


    @Column(name = "QC_CLS_SHT_DESC")
    private String clauseShortDescription;

    @Transient
    private ClauseDto clause;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        QuotationClause that = (QuotationClause) o;
        return clauseId != null && Objects.equals(clauseId, that.clauseId)
                && quotationProductId != null && Objects.equals(quotationProductId, that.quotationProductId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clauseId, quotationProductId);
    }
}
