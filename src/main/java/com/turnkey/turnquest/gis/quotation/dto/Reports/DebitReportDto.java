package com.turnkey.turnquest.gis.quotation.dto.Reports;

import lombok.Data;

@Data
public class DebitReportDto {

    private Long quotationId;

    private Long batchNo;

    private String iPayRef;

    private Long organizationId;

}
