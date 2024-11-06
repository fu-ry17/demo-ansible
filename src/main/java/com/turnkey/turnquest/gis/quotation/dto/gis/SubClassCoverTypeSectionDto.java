package com.turnkey.turnquest.gis.quotation.dto.gis;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubClassCoverTypeSectionDto {
    private Long id;

    private String code;

    private Long subClassId;

    private Long coverTypeId;

    private SectionDto section;

    private Long subClassCoverTypeId;

    private String sectionShortDescription;

    private Integer order;

    private String mandatory;

    private Integer calcGroup;

    private List<PremiumRateDto> premiumRates;
}
