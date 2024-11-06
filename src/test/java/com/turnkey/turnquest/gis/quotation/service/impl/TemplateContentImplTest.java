package com.turnkey.turnquest.gis.quotation.service.impl;

import com.turnkey.turnquest.gis.quotation.dto.Reports.Quote;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

/**
 * @author Titus Murithi Bundi
 */
class TemplateContentImplTest {


    @Mock
    private TemplateEngine templateEngine;

    @InjectMocks
    private TemplateContentImpl templateContent;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getTemplateContentReturnsExpectedString() {
        Quote quote = new Quote();
        when(templateEngine.process(eq("quot_summary"), any(Context.class))).thenReturn("Expected String");

        String result = templateContent.getTemplateContent(quote);

        assertEquals("Expected String", result);
    }

    @Test
    void getTemplateContentReturnsEmptyStringWhenQuoteIsNull() {
        when(templateEngine.process(eq("quot_summary"), any(Context.class))).thenReturn("");

        String result = templateContent.getTemplateContent(null);

        assertEquals("", result);
    }

    @Test
    void getTemplateContentThrowsExceptionWhenTemplateEngineFails() {
        when(templateEngine.process(eq("quot_summary"), any(Context.class))).thenThrow(new RuntimeException());

        assertThrows(RuntimeException.class, () -> templateContent.getTemplateContent(new Quote()));
    }
}
