package com.turnkey.turnquest.gis.quotation.service.impl;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;





@Component
public class ConverterImpl implements Converter{

    private static final String BASE_URL = "http://localhost:";

    @Value("${server.port}")
    public String servicePort;
    @Override
    public ByteArrayOutputStream convert(String quotationHtml) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        //Enables locating of the css file and the logo image
        ConverterProperties converterProperties = new ConverterProperties();
        converterProperties.setBaseUri(BASE_URL + servicePort);

        HtmlConverter.convertToPdf(quotationHtml, byteArrayOutputStream, converterProperties);
        return byteArrayOutputStream;
    }
}
