package com.turnkey.turnquest.gis.quotation.dto.underwriting;

import lombok.Data;

import java.io.Serializable;

@Data
public class PolicyClauseDto implements Serializable {

    private Long clauseId;

    private String code;

    private Long subClassId;

    private String policyNumber;

    private String renewalEndorsementNumber;

    private Long policyId;

    private Long policyRiskId;

    private String clauseType;

    private String clauseWording;

    private String clauseEditable;

    private String isNew;

    private String clauseHeading;

    private String productShortDescription;

    private Long productId;

}
