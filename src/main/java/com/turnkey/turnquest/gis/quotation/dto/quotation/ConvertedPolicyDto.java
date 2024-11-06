package com.turnkey.turnquest.gis.quotation.dto.quotation;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data

public class ConvertedPolicyDto {

    String policyNumber;

    Long policyId;

    Long quotationId;

    String quotationNumber;


}
