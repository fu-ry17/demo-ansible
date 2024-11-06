package com.turnkey.turnquest.gis.quotation.aki.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CertificateResponseDto {

    @JsonProperty("Inputs")
    private String inputs;

    private Boolean success;

    @JsonProperty("Error")
    private List<ErrorDto> errors;

    @JsonProperty("APIRequestNumber")
    private String apiRequestNumber;

    @JsonProperty("DMVICRefNo")
    private String referenceNo;
}
