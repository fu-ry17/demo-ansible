package com.turnkey.turnquest.gis.quotation.dto.gis;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SectionDto implements Serializable{
    private Long id;

    private String code;

    private String description;

    private String type;

    private List<SubClassSectionPerilDto> subClassSectionPerils;
}
