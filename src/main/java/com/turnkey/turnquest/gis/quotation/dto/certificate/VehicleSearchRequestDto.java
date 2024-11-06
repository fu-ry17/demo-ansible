package com.turnkey.turnquest.gis.quotation.dto.certificate;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VehicleSearchRequestDto {
    @JsonProperty("VehicleRegistrationNumber")
    private String vehicleRegistrationNumber;
}
