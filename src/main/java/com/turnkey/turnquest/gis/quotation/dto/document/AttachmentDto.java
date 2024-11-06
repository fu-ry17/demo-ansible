package com.turnkey.turnquest.gis.quotation.dto.document;

import lombok.Data;
import org.springframework.core.io.Resource;

@Data
public class AttachmentDto {

    private Resource file;

    private Long insurerOrgId;

    private Long clientId;

    private String policyNo;
}
