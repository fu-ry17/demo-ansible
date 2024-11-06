package com.turnkey.turnquest.gis.quotation.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * @author Titus Murithi Bundi
 */
class TemplateConfigurationTest {

    private TemplateConfiguration templateConfiguration;

    @BeforeEach
    void setUp() {
        templateConfiguration = new TemplateConfiguration();
    }

    @Test
    void shouldReturnTemplateResolverWithCorrectProperties() {
        ClassLoaderTemplateResolver resolver = templateConfiguration.templateResolver();

        assertEquals("templates/", resolver.getPrefix());
        assertFalse(resolver.isCacheable());
        assertEquals(".html", resolver.getSuffix());
        assertEquals("HTML", resolver.getTemplateMode().name());
        assertEquals("UTF-8", resolver.getCharacterEncoding());
    }
}
