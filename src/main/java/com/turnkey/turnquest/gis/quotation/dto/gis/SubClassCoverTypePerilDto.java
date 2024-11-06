package com.turnkey.turnquest.gis.quotation.dto.gis;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class SubClassCoverTypePerilDto implements Serializable {
    private Long id;

    private Long subClassId;

    private Long perilId;

    private String mandatory;

    private BigDecimal perilLimit;

    private String perilType;

    private String siOrLimit;

    private String excessType;

    private BigDecimal excess;

    private BigDecimal excessMinimum;

    private BigDecimal excessMaximum;

    private String expireOnClaim;

    private Long binderId;

    private BigDecimal personLimit;

    private BigDecimal claimLimit;

    private String description;

    private PerilDto peril;
}
