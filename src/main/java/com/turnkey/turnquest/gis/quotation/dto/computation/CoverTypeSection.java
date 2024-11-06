package com.turnkey.turnquest.gis.quotation.dto.computation;

import com.turnkey.turnquest.gis.quotation.enums.BenefitType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CoverTypeSection {
    private BenefitType benefitType;
    private Long id;
    private String sectionCode;
    private String sectionDescription;
    private Long sectionId;
    private BigDecimal limitAmount;
    private Long coverTypeSectionId;
}
