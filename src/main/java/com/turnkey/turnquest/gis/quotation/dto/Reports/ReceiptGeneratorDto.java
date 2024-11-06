package com.turnkey.turnquest.gis.quotation.dto.Reports;

import lombok.Data;

@Data
public class ReceiptGeneratorDto {

    private Long receiptId;

    private String policyNo;

    private Long quotationId;

    private Long policyId;

}
