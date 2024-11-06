package com.turnkey.turnquest.gis.quotation.dto.quotation;

import com.turnkey.turnquest.gis.quotation.enums.BenefitType;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class InitialSectionDto {
    private Long coverTypeSectionId;
    private BigDecimal limitAmount;
    private BenefitType benefitType;
}
