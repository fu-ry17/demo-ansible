package com.turnkey.turnquest.gis.quotation.dto.gis;

import lombok.Data;

import java.io.Serializable;

@Data
public class SectionShortPeriodFilterDto implements Serializable {

    private static final long serialVersionUID = -8783408876341191092L;

    private Long subClassId;

    private Long sectionId;

    private Long coverDays;
}
