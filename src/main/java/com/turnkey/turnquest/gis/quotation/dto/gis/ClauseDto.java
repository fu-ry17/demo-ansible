package com.turnkey.turnquest.gis.quotation.dto.gis;

import lombok.Data;

import java.io.Serializable;

@Data
public class ClauseDto implements Serializable {

    private static final long serialVersionUID = -3714578374191018175L;
    private Long id;

    private String code;

    private String heading;

    private String wording;

    private String type;

    private String editable;

    private String wordings;

    private String current;

    private String lien;

    private String ins;

    private String merge;

    private Long organizationId;
}
