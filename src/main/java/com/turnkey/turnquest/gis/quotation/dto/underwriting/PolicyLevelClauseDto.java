package com.turnkey.turnquest.gis.quotation.dto.underwriting;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PolicyLevelClauseDto {
    private Long clauseId;

    private Long productId;

    private String productShortDescription;

    private String clauseShortDescription;

    private Long subClassId;

    private String policyNumber;

    private String renewalEndorsementNumber;

    private Long policyId;

    private String clauseType;

    private String clauseWording;

    private String clauseEditable;

    private String isNew;

    private String clauseHeading;

    private String clauseHeader;

    private BigDecimal rowNumber;

    private String productApplicable;
}
