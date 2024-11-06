package com.turnkey.turnquest.gis.quotation.dto.gis;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class SubClassCoverTypeDto implements Serializable {

    private static final long serialVersionUID = -7575975375444254561L;
    private Long id;

    private CoverTypeDto coverType;

    private SubClassDto subClass;

    private String description;

    private String code;

    private BigDecimal minimumPremium;


}
