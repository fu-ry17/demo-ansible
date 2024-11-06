package com.turnkey.turnquest.gis.quotation.model;

import com.turnkey.turnquest.gis.quotation.model.audit.BaseAudit;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.persistence.*;
import java.io.Serializable;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "GIN_QUOTATION_REVISIONS")
public class QuotationRevision extends BaseAudit implements Serializable {
    private static final long serialVersionUID = -830540585500462934L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gin-quotation-revisions-seq")
    @SequenceGenerator(name="gin-quotation-revisions-seq", sequenceName = "GIN_QUOTATION_REVISIONS_SEQ", allocationSize = 1)
    @Column(name = "QUOTR_CODE")
    private Long id;

    @Column(name = "QUOTR_QUOT_CODE")
    private Long quotationId;

    @Column(name = "QUOTR_QUOT_NO")
    private String quotationNumber;

    @Column(name = "QUOTR_REVISED_QUOT_CODE")
    private Long revisedQuotationCode;
}
