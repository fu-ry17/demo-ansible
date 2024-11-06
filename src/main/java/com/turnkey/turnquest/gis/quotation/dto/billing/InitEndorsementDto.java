package com.turnkey.turnquest.gis.quotation.dto.billing;

import lombok.Data;

@Data
public class InitEndorsementDto {
    private Long policyId;
    private Long quotationId;
    private Long receiptId;
}
