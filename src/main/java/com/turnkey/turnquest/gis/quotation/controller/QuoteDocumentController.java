package com.turnkey.turnquest.gis.quotation.controller;


import com.turnkey.turnquest.gis.quotation.auth.TokenUtils;
import com.turnkey.turnquest.gis.quotation.model.QuoteDocument;
import com.turnkey.turnquest.gis.quotation.service.QuoteDocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/quote-documents")
public class QuoteDocumentController {

    private final QuoteDocumentService quoteDocumentService;
    private final TokenUtils tokenUtils;

    @Autowired
    public QuoteDocumentController(QuoteDocumentService quoteDocumentService, TokenUtils tokenUtils) {
        this.quoteDocumentService = quoteDocumentService;
        this.tokenUtils = tokenUtils;
    }

    @RolesAllowed({"quot_docs_get_one","agent"})
    @GetMapping("/{id}")
    public ResponseEntity<QuoteDocument> find(@PathVariable("id") Long id, Authentication authentication) throws Exception {
        tokenUtils.init(authentication);
        Optional<QuoteDocument> optionalQuotation = quoteDocumentService.findById(id, tokenUtils.getOrganizationId());
        return optionalQuotation
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }

    @RolesAllowed({"quot_docs_save","agent"})
    @PostMapping("/save")
    public ResponseEntity<QuoteDocument> add(@RequestBody QuoteDocument quoteDocument, Authentication authentication) throws Exception {
        tokenUtils.init(authentication);
        return ResponseEntity.ok(quoteDocumentService.save(quoteDocument, tokenUtils.getOrganizationId()));
    }

    @RolesAllowed({"quot_docs_save_all","agent"})
    @PostMapping("/save/all")
    public ResponseEntity<List<QuoteDocument>> addMany(@RequestBody List<QuoteDocument> quoteDocuments, Authentication authentication) throws Exception {
        tokenUtils.init(authentication);
        return ResponseEntity.ok( quoteDocumentService.saveAll(quoteDocuments, tokenUtils.getOrganizationId()));
    }

    @RolesAllowed({"quot_docs_get_by_quote_no","agent"})
    @GetMapping("find-by/quotation-no")
    public ResponseEntity<List<QuoteDocument>> findByQuotationNo(@RequestParam("quotationNo") String quotationNo, Authentication authentication){
        tokenUtils.init(authentication);
        return ResponseEntity.ok(quoteDocumentService.findByQuotationNo(quotationNo, tokenUtils.getOrganizationId()));
    }

    @RolesAllowed({"quot_docs_get_quote_id","agent"})
    @GetMapping("find-by/quotation-id/{quotationId}")
    public ResponseEntity<List<QuoteDocument>> findByQuotationId(@PathVariable Long quotationId, Authentication authentication){
        tokenUtils.init(authentication);
        return ResponseEntity.ok(quoteDocumentService.findByQuotationId(quotationId, tokenUtils.getOrganizationId()));
    }

    @RolesAllowed({"quot_docs_get_all","agent"})
    @GetMapping
    public ResponseEntity<List<QuoteDocument>> findAllQuoteDocuments(Authentication authentication){
        tokenUtils.init(authentication);
        return ResponseEntity.ok(quoteDocumentService.findAll(tokenUtils.getOrganizationId()));
    }
}
