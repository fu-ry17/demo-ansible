package com.turnkey.turnquest.gis.quotation.dto.gis;

import lombok.Data;

import java.io.Serializable;

@Data
public class SubClassSectionPerilDto implements Serializable {
    private Long id;

    private Long subClassId;

    private Long sectionId;

    private Long perilId;

    private Long subClassSection;

    private String binderType;

    private Long binderId;

    private Long subClassPerilId;

    private SubClassCoverTypePerilDto subClassCoverTypePeril;
}
