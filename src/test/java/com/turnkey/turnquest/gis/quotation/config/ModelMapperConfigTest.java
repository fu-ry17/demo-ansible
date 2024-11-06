package com.turnkey.turnquest.gis.quotation.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class ModelMapperConfigTest {

    private ModelMapperConfig modelMapperConfig;
    private ApplicationContext context;

    @BeforeEach
    void setUp() {
        modelMapperConfig = new ModelMapperConfig();
        context = mock(ApplicationContext.class);
    }

    @Test
    void shouldReturnModelMapperBean() {
        ModelMapper modelMapper = modelMapperConfig.modelMapper();
        assertNotNull(modelMapper);
    }

    @Test
    void shouldReturnObjectMapperBean() {
        ObjectMapper objectMapper = modelMapperConfig.objectMapper();
        assertNotNull(objectMapper);
        assertFalse(objectMapper.isEnabled(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES));
    }
}