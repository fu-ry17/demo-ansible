package com.turnkey.turnquest.gis.quotation.dto.requests;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.Date;

@Data
public class ConvertQuotationDto {

    @JsonIgnore
    private Long withEffectFromDate;

    @JsonIgnore
    private Long withEffectToDate;

    private String policyStatus;

    private Boolean policyRenewable;
}
