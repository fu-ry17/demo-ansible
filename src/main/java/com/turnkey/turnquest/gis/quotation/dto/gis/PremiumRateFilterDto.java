package com.turnkey.turnquest.gis.quotation.dto.gis;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PremiumRateFilterDto {
    List<Long> binderIds;
    List<Long> subClassSectionIds;
    List<Long> subClassIds;
    Boolean includeDiscount;
}
