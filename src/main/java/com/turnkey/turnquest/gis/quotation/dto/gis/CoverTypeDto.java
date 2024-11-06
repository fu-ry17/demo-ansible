package com.turnkey.turnquest.gis.quotation.dto.gis;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
public class CoverTypeDto implements Serializable {
    private static final long serialVersionUID = -4409418153376149244L;
    private Long id;

    private String code;

    private String description;

    private String details;

    private BigDecimal minSumInsured;

    private String downgradeOnSus;

    private String downgradeOnSusTo;

    private List<SubClassCoverTypeDto> subClassCoverTypes;
}
