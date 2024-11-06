package com.turnkey.turnquest.gis.quotation.dto.gis;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;

@Data
public class SubClassDto implements Serializable {

    private static final long serialVersionUID = 6653766934917293672L;

    private Long id;

    private String description;

    private BigDecimal maximumCashBackLevel;

    private BigDecimal maximumNcdLevel;

    private Collection<SubClassCoverTypeDto> subClassCoverTypes;

    private Collection<ProductSubClassDto> productSubClasses;

    private Collection<BinderDto> binders;

    private String autoGenerateCertificate;
}
