package com.turnkey.turnquest.gis.quotation.aki;

import com.turnkey.turnquest.gis.quotation.aki.dto.ValidationResponseDto;

public interface ValidateRiskService {

    ValidationResponseDto validateRisk(Long quotationId);

}
