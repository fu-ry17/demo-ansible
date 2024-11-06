package com.turnkey.turnquest.gis.quotation.dto.quotation;

import lombok.Data;

@Data
public class ResendReportRequestDto {
    private String clientName;
    private String clientEmail;
    private Long insurerOrgId;
    private Long quotationId;
    private String remarks;

}
