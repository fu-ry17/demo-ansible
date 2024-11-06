package com.turnkey.turnquest.gis.quotation.dto.quotation;

import lombok.Data;

import java.util.List;

@Data
public class QuickQuotationDto {

    private Long productGroupId;

    private Long coverTypeId;

    private Long coverFromDate;

    private Long coverToDate;

    private Long currencyId;

    private String riskId;

    private String yearOfManufacture;

    private Long organizationId;

    private Long insurerOrgId;

    private List<InitialSectionDto> initialSections;

    private List<QuickQuotationRiskSectionDto> quickQuotationRiskSections;

}

