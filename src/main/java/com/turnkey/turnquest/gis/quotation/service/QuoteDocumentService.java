package com.turnkey.turnquest.gis.quotation.service;

import com.turnkey.turnquest.gis.quotation.dto.underwriting.PolicyDocumentsDto;
import com.turnkey.turnquest.gis.quotation.model.QuotationRisk;
import com.turnkey.turnquest.gis.quotation.model.QuoteDocument;

import java.util.List;
import java.util.Optional;

public interface QuoteDocumentService {

    Optional<QuoteDocument> findById(Long id, Long organizationId)  throws Exception;

    List<QuoteDocument> findAll(Long organizationId);

    QuoteDocument save(QuoteDocument quoteDocument, Long organizationId);

    List<QuoteDocument> saveAll(List<QuoteDocument> quoteDocuments, Long organizationId);

    List<QuoteDocument> findByQuotationNo(String quotationNo, Long organizationId);

    List<QuoteDocument> findByQuotationId(Long quotationId, Long organizationId);

    List<PolicyDocumentsDto> convertToPolicyDocuments(List<QuoteDocument> quoteDocuments);


}
