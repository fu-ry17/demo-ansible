package com.turnkey.turnquest.gis.quotation.controller;

import com.turnkey.turnquest.gis.quotation.model.QuotationRiskSection;
import com.turnkey.turnquest.gis.quotation.service.QuotationRiskSectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/quotation-risk-sections")
public class QuotationRiskSectionController {

    private final QuotationRiskSectionService quotationRiskSectionService;

    @Autowired
    public QuotationRiskSectionController(QuotationRiskSectionService quotationRiskSectionService) {
        this.quotationRiskSectionService = quotationRiskSectionService;
    }

    @RolesAllowed("quot_risk_sect_create")
    @PostMapping()
    public ResponseEntity<QuotationRiskSection> create(@RequestBody QuotationRiskSection quotationRiskSection) {
        return ResponseEntity.ok(this.quotationRiskSectionService.create(quotationRiskSection));
    }

    @RolesAllowed("quot_risk_sect_create_multiple")
    @PostMapping("/multiple")
    public ResponseEntity<List<QuotationRiskSection>> createMultiple(@RequestBody List<QuotationRiskSection> quotationRiskSections) {
        return ResponseEntity.ok(this.quotationRiskSectionService.createMultiple(quotationRiskSections));
    }

    @RolesAllowed("quot_risk_sect_update")
    @PutMapping("/{id}")
    public ResponseEntity<QuotationRiskSection> update(@RequestBody QuotationRiskSection quotationRiskSection, @PathVariable("id") Long id) {
        return ResponseEntity.ok(this.quotationRiskSectionService.update(quotationRiskSection, id));
    }

    @RolesAllowed("quot_risk_sect_delete")
    @DeleteMapping("/{id}")
    public ResponseEntity<QuotationRiskSection> delete(@PathVariable("id") Long id) {
        quotationRiskSectionService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @RolesAllowed("quot_risk_sect_get_one")
    @GetMapping("/{id}")
    public ResponseEntity<QuotationRiskSection> find(@PathVariable("id") Long id) {
        Optional<QuotationRiskSection> optionalQuotationRiskSection = this.quotationRiskSectionService.find(id);
        return optionalQuotationRiskSection
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
