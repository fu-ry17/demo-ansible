package com.turnkey.turnquest.gis.quotation.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.turnkey.turnquest.gis.quotation.model.audit.BaseAudit;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;


@Data
@Entity
@ToString(exclude = "quotation")
@EqualsAndHashCode(callSuper = true)
@Table(name = "GIN_QUOTATION_AGENCIES")
public class QuotationAgency extends BaseAudit implements Serializable {

    private static final long serialVersionUID = -1540229101205370919L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gin-quotation-agencies-seq")
    @SequenceGenerator(name="gin-quotation-agencies-seq", sequenceName = "GIN_QUOTATION_AGENCIES_SEQ", allocationSize = 1)
    @Column(name = "QUA_CODE")
    private Long id;

    @Column(name = "QUA_QUOT_CODE")
    private Long quotationId;

    @OneToOne
    @JsonIgnoreProperties({"quotationAgency"})
    @JoinColumn(name = "QUA_QUOT_CODE", insertable = false, updatable = false)
    private Quotation quotation;

    @Column(name = "QUA_AGN_CODE")
    private Long agentId;

    @Column(name = "QUA_AGN_ASGN")
    private String agentAssigned;

    @Column(name = "QUA_AUTHORIZED")
    private String authorized;

}
