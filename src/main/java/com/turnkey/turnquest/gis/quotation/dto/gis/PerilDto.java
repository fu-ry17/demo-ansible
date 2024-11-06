package com.turnkey.turnquest.gis.quotation.dto.gis;

import lombok.Data;

import java.io.Serializable;

@Data
public class PerilDto implements Serializable {
    private Long id;

    private String description;

    private String fullDescription;

    private String paymentPerType;

    private Long withEffectFromDate;

    private Long withEffectToDate;

    private String perilType;
}
