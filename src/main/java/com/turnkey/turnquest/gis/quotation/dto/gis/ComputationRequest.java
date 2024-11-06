package com.turnkey.turnquest.gis.quotation.dto.gis;

import com.turnkey.turnquest.gis.quotation.dto.computation.CoverTypeSection;
import com.turnkey.turnquest.gis.quotation.dto.quotation.QuotationRiskSectionDto;
import com.turnkey.turnquest.gis.quotation.model.QuotationRiskSection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ComputationRequest {
    private List<QuotationRiskSectionDto> quickQuotationRiskSections;
    private List<CoverTypeSection> initialSections;
    private Long currencyId;
    private String riskId;
    private Long coverFromDate;
    private Long coverToDate;
    private Long yearOfManufacture;
    private Long productGroupId;
    private Long coverTypeId;
    private Long insurerOrgId;
    private BigDecimal sumInsured;
    private Long organizationId;
}
