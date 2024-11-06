package com.turnkey.turnquest.gis.quotation.dto.gis;

import lombok.Data;

import java.io.Serializable;

@Data
public class GisParameterDto implements Serializable {
    private static final long serialVersionUID = 8296027299889702974L;
    private Long id;

    private String name;

    private String status;

    private String value;

    private String description;
}
