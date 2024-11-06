package com.turnkey.turnquest.gis.quotation.dto.todo;

import lombok.Data;

@Data
public class AttachmentDto {
    private Long id;

    private String category;

    private String directoryId;

    private String mimeType;

    private String fileName;

    private String newFileName;

    private Long organizationId;
}
