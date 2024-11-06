package com.turnkey.turnquest.gis.quotation.dto.document;

import lombok.Data;

@Data
public class S3MetaData{

    private Long lastModified;

    private Long createdAt;

    private String mimeType = "";

    private Long size = 0L;

}
