package com.turnkey.turnquest.gis.quotation.dto.todo;

import com.turnkey.turnquest.gis.quotation.enums.LinkType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TodoLinkDto {
    LinkType linkType;

    Long linkId;
}
