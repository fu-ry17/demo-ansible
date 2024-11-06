package com.turnkey.turnquest.gis.quotation.controller.unit;

import com.turnkey.turnquest.gis.quotation.auth.TokenUtils;
import com.turnkey.turnquest.gis.quotation.controller.QuoteDocumentController;
import com.turnkey.turnquest.gis.quotation.model.QuoteDocument;
import com.turnkey.turnquest.gis.quotation.service.QuoteDocumentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * @author Titus Murithi Bundi
 */
class QuoteDocumentControllerTest {

    @Mock
    private QuoteDocumentService quoteDocumentService;

    @Mock
    private TokenUtils tokenUtils;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private QuoteDocumentController quoteDocumentController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnQuoteDocumentById() throws Exception {
        QuoteDocument quoteDocument = new QuoteDocument();

        when(tokenUtils.getOrganizationId()).thenReturn(1L);
        when(quoteDocumentService.findById(1L, 1L)).thenReturn(Optional.of(quoteDocument));

        ResponseEntity<QuoteDocument> response = quoteDocumentController.find(1L, authentication);

        assertEquals(quoteDocument, response.getBody());
    }

    @Test
    void shouldReturnNoContentWhenQuoteDocumentNotFound() throws Exception {
        when(tokenUtils.getOrganizationId()).thenReturn(1L);
        when(quoteDocumentService.findById(1L, 1L)).thenReturn(Optional.empty());

        ResponseEntity<QuoteDocument> response = quoteDocumentController.find(1L, authentication);

        assertEquals(204, response.getStatusCodeValue());
    }

    @Test
    void shouldSaveQuoteDocument() throws Exception {
        QuoteDocument quoteDocument = new QuoteDocument();

        when(tokenUtils.getOrganizationId()).thenReturn(1L);
        when(quoteDocumentService.save(quoteDocument, 1L)).thenReturn(quoteDocument);

        ResponseEntity<QuoteDocument> response = quoteDocumentController.add(quoteDocument, authentication);

        assertEquals(quoteDocument, response.getBody());
    }

    @Test
    void shouldReturnQuoteDocumentsByQuotationNo() {
        QuoteDocument quoteDocument1 = new QuoteDocument();
        QuoteDocument quoteDocument2 = new QuoteDocument();
        List<QuoteDocument> quoteDocuments = Arrays.asList(quoteDocument1, quoteDocument2);

        when(tokenUtils.getOrganizationId()).thenReturn(1L);
        when(quoteDocumentService.findByQuotationNo("123", 1L)).thenReturn(quoteDocuments);

        ResponseEntity<List<QuoteDocument>> response = quoteDocumentController.findByQuotationNo("123", authentication);

        assertEquals(quoteDocuments, response.getBody());
    }

    @Test
    void shouldReturnQuoteDocumentsByQuotationId() {
        QuoteDocument quoteDocument1 = new QuoteDocument();
        QuoteDocument quoteDocument2 = new QuoteDocument();
        List<QuoteDocument> quoteDocuments = Arrays.asList(quoteDocument1, quoteDocument2);

        when(tokenUtils.getOrganizationId()).thenReturn(1L);
        when(quoteDocumentService.findByQuotationId(1L, 1L)).thenReturn(quoteDocuments);

        ResponseEntity<List<QuoteDocument>> response = quoteDocumentController.findByQuotationId(1L, authentication);

        assertEquals(quoteDocuments, response.getBody());
    }

    @Test
    void shouldReturnAllQuoteDocuments() {
        QuoteDocument quoteDocument1 = new QuoteDocument();
        QuoteDocument quoteDocument2 = new QuoteDocument();
        List<QuoteDocument> quoteDocuments = Arrays.asList(quoteDocument1, quoteDocument2);

        when(tokenUtils.getOrganizationId()).thenReturn(1L);
        when(quoteDocumentService.findAll(1L)).thenReturn(quoteDocuments);

        ResponseEntity<List<QuoteDocument>> response = quoteDocumentController.findAllQuoteDocuments(authentication);

        assertEquals(quoteDocuments, response.getBody());
    }

}
