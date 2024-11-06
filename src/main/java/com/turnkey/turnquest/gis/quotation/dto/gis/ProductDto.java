package com.turnkey.turnquest.gis.quotation.dto.gis;

import com.turnkey.turnquest.gis.quotation.dto.gis.BinderDto;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class ProductDto implements Serializable {

    private static final long serialVersionUID = 3199257926922200891L;
    private Long id;

    private String code;

    private String description;

    private Date withEffectFromDate;

    private Date withEffectToDate;

    private String shortName;

    private String interfaceType;

    private String policyPrefix;

    private String claimPrefix;

    private String expiryPeriod;

    private BinderDto binder;

    private BigDecimal endorsementMinimumPremium;

    private Long newProductCode;

    private Long organizationId;

    private String accomodation;

    private List<ProductInstallmentDto> productInstallments;
}
