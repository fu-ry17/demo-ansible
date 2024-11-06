package com.turnkey.turnquest.gis.quotation.dto.computation;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SubClassCoverTypeSection {
    private BigDecimal id;
    private String coverTypeShortDescription;
    private Long subClassId;
    private Long coverTypeId;
    private Long sectionId;
    private SubClassSection subClassSection;
    private Long subClassCoverTypeId;
    private Long order;
    private String mandatory;
    private Long calcGroup;
    private Long productSubClassId;
    private Long subClassSectionId;

}
