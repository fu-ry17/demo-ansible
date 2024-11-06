package com.turnkey.turnquest.gis.quotation.config;


import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("dmvic")
@RequiredArgsConstructor
@Data
public class DmvicValidation {
    private Boolean validation;
    private Boolean vehicleDetail;
}
