package com.turnkey.turnquest.gis.quotation.dto.underwriting;

import lombok.Data;

@Data
public class PolicyDocumentsDto {

    private Long id;

    private String documentRef;

    private Long documentId;

    private Long policyRiskId;

    private Long policyId;

    private Long organizationId;
}
