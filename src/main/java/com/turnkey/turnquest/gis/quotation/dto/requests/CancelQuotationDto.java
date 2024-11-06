package com.turnkey.turnquest.gis.quotation.dto.requests;

import lombok.Data;

@Data
public class CancelQuotationDto {
    private String status;
    private String cancelReason;
}
