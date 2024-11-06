package com.turnkey.turnquest.gis.quotation.dto.underwriting;

import lombok.Data;

@Data
public class RiskCertUpdateDto {
    private Long quotationId;

    private String registrationNo;

    private Long policyRiskId;

    private String certificateNo;

    private String policyNo;
}
