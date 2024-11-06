package com.turnkey.turnquest.gis.quotation.dto.gis;

import com.turnkey.turnquest.gis.quotation.dto.quotation.InstallmentDto;
import com.turnkey.turnquest.gis.quotation.enums.InstallmentCalculation;
import com.turnkey.turnquest.gis.quotation.enums.InstallmentCoverGaps;
import lombok.Data;

@Data
public class ProductInstallmentDto {

    private Long id;

    private InstallmentCalculation calculation;

    private String installmentDistribution;

    private String certificateDistribution;

    private String commissionDistribution;

    private InstallmentCoverGaps coverGaps;

    private Long installmentPlan;

    private Long productId;

    private InstallmentDto installmentDto;
}
