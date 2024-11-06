package com.turnkey.turnquest.gis.quotation.service.impl;

import com.turnkey.turnquest.gis.quotation.dto.underwriting.PolicyDocumentsDto;
import com.turnkey.turnquest.gis.quotation.model.QuoteDocument;
import com.turnkey.turnquest.gis.quotation.repository.QuoteDocumentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * @author Titus Murithi Bundi
 */
class QuoteDocumentServiceImplTest {

    @Mock
    private QuoteDocumentRepository quoteDocumentRepository;

    @InjectMocks
    private QuoteDocumentServiceImpl quoteDocumentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findByIdReturnsQuoteDocument() {
        QuoteDocument quoteDocument = new QuoteDocument();
        when(quoteDocumentRepository.findByIdAndOrganizationId(any(Long.class), any(Long.class))).thenReturn(Optional.of(quoteDocument));

        Optional<QuoteDocument> result = quoteDocumentService.findById(1L, 1L);

        assertEquals(quoteDocument, result.orElse(null));
    }

    @Test
    void findAllReturnsQuoteDocuments() {
        List<QuoteDocument> quoteDocuments = Arrays.asList(new QuoteDocument(), new QuoteDocument());
        when(quoteDocumentRepository.findAllByOrganizationId(any(Long.class))).thenReturn(quoteDocuments);

        List<QuoteDocument> result = quoteDocumentService.findAll(1L);

        assertEquals(quoteDocuments, result);
    }

    @Test
    void saveReturnsSavedQuoteDocument() {
        QuoteDocument quoteDocument = new QuoteDocument();
        when(quoteDocumentRepository.save(any(QuoteDocument.class))).thenReturn(quoteDocument);

        QuoteDocument result = quoteDocumentService.save(new QuoteDocument(), 1L);

        assertEquals(quoteDocument, result);
    }

    @Test
    void saveAllReturnsSavedQuoteDocuments() {
        List<QuoteDocument> quoteDocuments = Arrays.asList(new QuoteDocument(), new QuoteDocument());
        when(quoteDocumentRepository.saveAll(anyList())).thenReturn(quoteDocuments);

        List<QuoteDocument> result = quoteDocumentService.saveAll(Arrays.asList(new QuoteDocument(), new QuoteDocument()), 1L);

        assertEquals(quoteDocuments, result);
    }

    @Test
    void findByQuotationNoReturnsQuoteDocuments() {
        List<QuoteDocument> quoteDocuments = Arrays.asList(new QuoteDocument(), new QuoteDocument());
        when(quoteDocumentRepository.findAllByQuotationNoAndOrganizationId(anyString(), any(Long.class))).thenReturn(quoteDocuments);

        List<QuoteDocument> result = quoteDocumentService.findByQuotationNo("123", 1L);

        assertEquals(quoteDocuments, result);
    }

    @Test
    void findByQuotationIdReturnsQuoteDocuments() {
        List<QuoteDocument> quoteDocuments = Arrays.asList(new QuoteDocument(), new QuoteDocument());
        when(quoteDocumentRepository.findAllByOrganizationIdAndQuotationId(any(Long.class), any(Long.class))).thenReturn(quoteDocuments);

        List<QuoteDocument> result = quoteDocumentService.findByQuotationId(1L, 1L);

        assertEquals(quoteDocuments, result);
    }

    @Test
    void convertToPolicyDocumentsReturnsPolicyDocuments() {
        List<QuoteDocument> quoteDocuments = Arrays.asList(new QuoteDocument(), new QuoteDocument());

        List<PolicyDocumentsDto> result = quoteDocumentService.convertToPolicyDocuments(quoteDocuments);

        assertEquals(quoteDocuments.size(), result.size());
    }
}
