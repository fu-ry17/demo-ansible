package com.turnkey.turnquest.gis.quotation.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = DmvicValidation.class)
@EnableConfigurationProperties
class DmvicValidationTest {

    private DmvicValidation dmvicValidation;

    @BeforeEach
    void setUp() {
        dmvicValidation = new DmvicValidation();
    }

    @Test
    void shouldSetAndGetValidation() {
        dmvicValidation.setValidation(true);
        assertTrue(dmvicValidation.getValidation());
    }

    @Test
    void shouldSetAndGetVehicleDetail() {
        dmvicValidation.setVehicleDetail(true);
        assertTrue(dmvicValidation.getVehicleDetail());
    }
}