package com.turnkey.turnquest.gis.quotation.service.impl;

import java.io.ByteArrayOutputStream;

public interface Converter {
    ByteArrayOutputStream convert(String quotationHtml);
}
