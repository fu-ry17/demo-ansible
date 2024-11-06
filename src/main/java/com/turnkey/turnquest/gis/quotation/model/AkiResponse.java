package com.turnkey.turnquest.gis.quotation.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.turnkey.turnquest.gis.quotation.model.audit.BaseAudit;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class AkiResponse extends BaseAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "aki-response-seq")
    @SequenceGenerator(name="aki-response-seq", sequenceName = "aki_response_seq", allocationSize = 1)
    private Long id;

    private String riskId;

    private Long quotationId;

    @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean success;

    @JsonIgnore
    @Column(columnDefinition = "TEXT NULL")
    private String errorsBlob;

    @Transient
    private List<String> errors = new ArrayList<>();

}
