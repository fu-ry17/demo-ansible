package com.turnkey.turnquest.gis.quotation.dto.crm;

import lombok.Data;

import java.io.Serializable;

@Data
public class DivisionDto implements Serializable {
    private static final long serialVersionUID = -1113186680909848292L;
    private Long id;

    private String name;

    private String code;

    private Long organizationId;
}
