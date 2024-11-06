package com.turnkey.turnquest.gis.quotation.controller;

import com.turnkey.turnquest.gis.quotation.model.QuotationRiskTax;
import com.turnkey.turnquest.gis.quotation.service.QuotationRiskTaxService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("risk-taxes")
public class QuotationRiskTaxController {

    private final QuotationRiskTaxService quotationRiskTaxService;

    public QuotationRiskTaxController(QuotationRiskTaxService quotationRiskTaxService) {
        this.quotationRiskTaxService = quotationRiskTaxService;
    }

    @RolesAllowed("quot_risk_tax_get_one")
    @GetMapping("/{id}")
    public ResponseEntity<QuotationRiskTax> findById(@PathVariable Long id) {
        return quotationRiskTaxService.find(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @RolesAllowed("quot_risk_tax_create")
    @PostMapping("/create")
    public ResponseEntity<QuotationRiskTax> createRiskTax(@RequestBody QuotationRiskTax quotationRiskTax) {
        return ResponseEntity.ok(quotationRiskTaxService.create(quotationRiskTax));
    }

    @RolesAllowed("quot_risk_tax_update")
    @PostMapping("/update")
    public ResponseEntity<QuotationRiskTax> updateRiskTax(@RequestBody QuotationRiskTax quotationRiskTax) {
        return ResponseEntity.ok(quotationRiskTaxService.update(quotationRiskTax, quotationRiskTax.getQuotationRiskId()));
    }

    @RolesAllowed("quot_risk_tax_create")
    @PostMapping("/create/multiple")
    public ResponseEntity<List<QuotationRiskTax>> createMultipleRiskTax(@RequestBody List<QuotationRiskTax> quotationRiskTaxes) {
        return ResponseEntity.ok(quotationRiskTaxService.createMultiple(quotationRiskTaxes));
    }

    @RolesAllowed("quot_risk_tax_update")
    @PostMapping("/{id}/update-amount")
    public ResponseEntity<QuotationRiskTax> updateTaxAmount(@RequestBody BigDecimal amount, @PathVariable Long id) {
        return ResponseEntity.ok(quotationRiskTaxService.updateTaxAmount(amount, id));
    }
}
