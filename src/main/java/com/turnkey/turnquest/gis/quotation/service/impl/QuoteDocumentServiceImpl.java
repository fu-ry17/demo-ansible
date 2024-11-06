package com.turnkey.turnquest.gis.quotation.service.impl;

import com.turnkey.turnquest.gis.quotation.client.DocsService.DirectoryClient;
import com.turnkey.turnquest.gis.quotation.dto.underwriting.PolicyDocumentsDto;
import com.turnkey.turnquest.gis.quotation.model.QuoteDocument;
import com.turnkey.turnquest.gis.quotation.repository.QuoteDocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional
@Service
public class QuoteDocumentServiceImpl implements com.turnkey.turnquest.gis.quotation.service.QuoteDocumentService {

    public final QuoteDocumentRepository quoteDocumentRepository;
    public final DirectoryClient directoryClient;

    @Autowired
    public QuoteDocumentServiceImpl(QuoteDocumentRepository quoteDocumentRepository, DirectoryClient directoryClient) {
        this.quoteDocumentRepository = quoteDocumentRepository;
        this.directoryClient = directoryClient;
    }

    @Override
    public Optional<QuoteDocument> findById(Long id, Long organizationId) {
        return quoteDocumentRepository.findByIdAndOrganizationId(id, organizationId);
    }

    @Override
    public List<QuoteDocument> findAll(Long organizationId) {
        return quoteDocumentRepository.findAllByOrganizationId(organizationId);
    }

    @Override
    public QuoteDocument save(QuoteDocument quoteDocument, Long organizationId) {
        quoteDocument.setOrganizationId(organizationId);
        return quoteDocumentRepository.save(quoteDocument);
    }

    @Override
    public List<QuoteDocument> saveAll(List<QuoteDocument> quoteDocuments, Long organizationId) {
        quoteDocuments = quoteDocuments.stream()
                .peek(quoteDocument -> quoteDocument.setOrganizationId(organizationId))
                .collect(Collectors.toList());
        return quoteDocumentRepository.saveAll(quoteDocuments);
    }

    @Override
    public List<QuoteDocument> findByQuotationNo(String quotationNo, Long organizationId) {
        return quoteDocumentRepository.findAllByQuotationNoAndOrganizationId(quotationNo, organizationId);
    }

    @Override
    public List<QuoteDocument> findByQuotationId(Long quotationId, Long organizationId) {
        return quoteDocumentRepository.findAllByOrganizationIdAndQuotationId(organizationId, quotationId);
    }

    @Override
    public List<PolicyDocumentsDto> convertToPolicyDocuments(List<QuoteDocument> quoteDocuments) {

        List<PolicyDocumentsDto> policyDocuments = new LinkedList<>();
        for (QuoteDocument quoteDocument : quoteDocuments){
            policyDocuments.add(convertToPolicyDocument(quoteDocument));
        }
        return policyDocuments;
    }

    private PolicyDocumentsDto convertToPolicyDocument(QuoteDocument quoteDocument){
        PolicyDocumentsDto policyDocumentsDto = new PolicyDocumentsDto();
        policyDocumentsDto.setDocumentRef(quoteDocument.getDocument());
        policyDocumentsDto.setDocumentId(quoteDocument.getDocumentId());
        policyDocumentsDto.setOrganizationId(quoteDocument.getOrganizationId());

        return policyDocumentsDto;
    }
}
