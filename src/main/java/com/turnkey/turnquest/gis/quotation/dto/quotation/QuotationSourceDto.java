package com.turnkey.turnquest.gis.quotation.dto.quotation;


import lombok.Data;

@Data
public class QuotationSourceDto {

    private Long id;

    private String description;

    private String shortDescription;

    private Long organizationId;
}
