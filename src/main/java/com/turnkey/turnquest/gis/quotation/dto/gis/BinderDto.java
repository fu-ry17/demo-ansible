package com.turnkey.turnquest.gis.quotation.dto.gis;

import com.turnkey.turnquest.gis.quotation.dto.crm.OrganizationDto;
import lombok.Data;

import java.io.Serializable;

@Data
public class BinderDto implements Serializable {

    private static final long serialVersionUID = -8985694959015463475L;

    private Long id;

    private String name;

    private String remarks;

    private String type;

    private Long productId;

    private ProductDto product;

    private Long subClassId;

    private SubClassDto subClass;

    private Long currencyId;

    private String longTermAgreementType;

    private String commissionType;

    private OrganizationDto organization;
}
