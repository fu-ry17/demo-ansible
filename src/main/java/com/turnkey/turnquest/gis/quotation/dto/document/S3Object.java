package com.turnkey.turnquest.gis.quotation.dto.document;

import lombok.Data;

@Data

public class S3Object {

    private String fileName;

    private String key;

    private String url;

    private S3MetaData metaData;

}
