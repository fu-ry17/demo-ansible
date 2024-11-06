package com.turnkey.turnquest.gis.quotation.dto.certificate;

import lombok.Data;

@Data
public class VehicleSearchResponseDto {
    private String ipuCode;

    private String chasisNo;

    private String engineNo;

    private String make;

    private String model;

    private String bodyType;

    private Long yearOfManufacture;

    private String carryCapacity;
}
