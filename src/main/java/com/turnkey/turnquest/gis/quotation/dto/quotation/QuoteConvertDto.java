package com.turnkey.turnquest.gis.quotation.dto.quotation;

import lombok.Data;

@Data
public class QuoteConvertDto {

    private Long quotationId;

    private String quotationNo;

    private String iPayRefNo;

    private Long organizationId;

    private Long receiptId;

    private String sourceRef;
}
