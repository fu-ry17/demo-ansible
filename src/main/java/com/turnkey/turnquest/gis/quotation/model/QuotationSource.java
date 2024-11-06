/**
 *
 */
package com.turnkey.turnquest.gis.quotation.model;

import com.turnkey.turnquest.gis.quotation.model.audit.BaseAudit;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

import jakarta.persistence.*;

/**
 * @author Paul Gichure
 */
@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "GIN_QUOTATION_SOURCES")
public class QuotationSource extends BaseAudit implements Serializable {

    private static final long serialVersionUID = -4437426199061635926L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gin-quotation-sources-seq")
    @SequenceGenerator(name="gin-quotation-sources-seq", sequenceName = "GIN_QUOTATION_SOURCES_SEQ", allocationSize = 1)
    @Column(name = "QSC_CODE", nullable = false, unique = true)
    private Long id;

    @Column(name = "QSC_DESC", nullable = false)
    @NotNull
    private String description;

    @Column(name = "effective_date")
    private Long effectiveDate;


}
