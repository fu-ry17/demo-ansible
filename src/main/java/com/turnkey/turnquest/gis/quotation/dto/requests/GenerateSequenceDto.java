package com.turnkey.turnquest.gis.quotation.dto.requests;

import lombok.Data;

import java.io.Serializable;

@Data
public class GenerateSequenceDto implements Serializable {
    private static final long serialVersionUID = -5956250392139208323L;
    private String sequenceType;
    private String sequenceNumberType;
    private String prefix;
    private Long branchId;
    private Integer underwritingYear;
    private Long countryId;
    private Long organizationId;
}
