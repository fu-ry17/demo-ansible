package com.turnkey.turnquest.gis.quotation.dto.quotation;

import lombok.Data;

@Data
public class QuoteDocumentDto {
    private Long id;

    private String document;

    private Long documentId;

    private Long quotationRiskId;

    private Long quotationId;

    private String quotationNo;

    private Long organizationId;

    private boolean isValuationDocument;
}
