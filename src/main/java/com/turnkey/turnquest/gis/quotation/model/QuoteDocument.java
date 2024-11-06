package com.turnkey.turnquest.gis.quotation.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.turnkey.turnquest.gis.quotation.enums.YesNo;
import com.turnkey.turnquest.gis.quotation.model.audit.BaseAudit;
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
@Table(name = "GIN_QUOTE_DOCUMENTS")
@JsonIgnoreProperties(value = {"quotationRisk","quotationRisks","quoteDocument","quoteDocuments"},ignoreUnknown = true)
public class QuoteDocument extends BaseAudit implements Serializable {

    private static final long serialVersionUID = 3597085990987167099L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "quot-documents-seq")
    @SequenceGenerator(name="quot-documents-seq", sequenceName = "GIN_QUOTE_DOCUMENTS_SEQ", allocationSize = 1)
    @Column(name = "QD_ID")
    private Long id;

    @Column(name = "QD_REF")
    private String document;

    @Column(name = "QD_DOC_ID")
    private Long documentId;

    @Column(name = "QD_QR_ID")
    private Long quotationRiskId;

    @Column(name = "QD_QUOTE_ID")
    private Long quotationId;

    @Column(name = "QD_QUOTE_NO")
    private String quotationNo;

    @Column(name = "QD_ORGANIZATION_ID")
    private Long organizationId;

    @Column(name = "QD_DESC")
    private String description = "";

    @Column(name = "QD_IS_VALUATION_LETTER")
    private YesNo isValuationLetter = YesNo.N;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "QD_QR_ID", referencedColumnName = "id", insertable = false, updatable = false)
    private QuotationRisk quotationRisk;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        QuoteDocument that = (QuoteDocument) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
