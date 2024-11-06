package com.turnkey.turnquest.gis.quotation.dto.gis;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class FilterCoverTypeSectionsDto {
    private List<Long> coverTypeIds;
    private List<Long> sectionIds;
}
