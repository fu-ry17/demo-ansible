package com.turnkey.turnquest.gis.quotation.controller.unit;

import com.turnkey.turnquest.gis.quotation.controller.QuotationRiskSectionController;
import com.turnkey.turnquest.gis.quotation.model.QuotationRiskSection;
import com.turnkey.turnquest.gis.quotation.service.QuotationRiskSectionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

/**
 * @author Titus Murithi Bundi
 */
class QuotationRiskSectionControllerTest {

    @Mock
    private QuotationRiskSectionService quotationRiskSectionService;

    @InjectMocks
    private QuotationRiskSectionController quotationRiskSectionController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreateQuotationRiskSection() {
        QuotationRiskSection quotationRiskSection = new QuotationRiskSection();

        when(quotationRiskSectionService.create(quotationRiskSection)).thenReturn(quotationRiskSection);

        ResponseEntity<QuotationRiskSection> response = quotationRiskSectionController.create(quotationRiskSection);

        assertEquals(quotationRiskSection, response.getBody());
    }

    @Test
    void shouldCreateMultipleQuotationRiskSections() {
        QuotationRiskSection quotationRiskSection1 = new QuotationRiskSection();
        QuotationRiskSection quotationRiskSection2 = new QuotationRiskSection();
        List<QuotationRiskSection> quotationRiskSections = Arrays.asList(quotationRiskSection1, quotationRiskSection2);

        when(quotationRiskSectionService.createMultiple(quotationRiskSections)).thenReturn(quotationRiskSections);

        ResponseEntity<List<QuotationRiskSection>> response = quotationRiskSectionController.createMultiple(quotationRiskSections);

        assertEquals(quotationRiskSections, response.getBody());
    }

    @Test
    void shouldUpdateQuotationRiskSection() {
        QuotationRiskSection quotationRiskSection = new QuotationRiskSection();

        when(quotationRiskSectionService.update(quotationRiskSection, 1L)).thenReturn(quotationRiskSection);

        ResponseEntity<QuotationRiskSection> response = quotationRiskSectionController.update(quotationRiskSection, 1L);

        assertEquals(quotationRiskSection, response.getBody());
    }

    @Test
    void shouldFindQuotationRiskSection() {
        QuotationRiskSection quotationRiskSection = new QuotationRiskSection();

        when(quotationRiskSectionService.find(1L)).thenReturn(Optional.of(quotationRiskSection));

        ResponseEntity<QuotationRiskSection> response = quotationRiskSectionController.find(1L);

        assertEquals(quotationRiskSection, response.getBody());
    }

    @Test
    void shouldReturnNotFoundWhenQuotationRiskSectionNotFound() {
        when(quotationRiskSectionService.find(1L)).thenReturn(Optional.empty());

        ResponseEntity<QuotationRiskSection> response = quotationRiskSectionController.find(1L);

        assertEquals(404, response.getStatusCodeValue());
    }

}
