package com.turnkey.turnquest.gis.quotation.controller;

import com.turnkey.turnquest.gis.quotation.model.QuotationRisk;
import com.turnkey.turnquest.gis.quotation.model.QuotationRiskSection;
import com.turnkey.turnquest.gis.quotation.model.QuotationRiskTax;
import com.turnkey.turnquest.gis.quotation.service.QuotationRiskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;
import java.util.Optional;

/**
 *
 */
@RestController
@RequestMapping(value = "/quotation-risks")
public class QuotationRiskController {

    private final QuotationRiskService quotationRiskService;

    @Autowired
    public QuotationRiskController(QuotationRiskService quotationRiskService) {
        this.quotationRiskService = quotationRiskService;
    }

    /**
     * @param quotationRisk 8
     * @return
     */
    @RolesAllowed("quot_risk_create")
    @PostMapping()
    public ResponseEntity<QuotationRisk> create(@RequestBody QuotationRisk quotationRisk) {
        return ResponseEntity.ok(quotationRiskService.create(quotationRisk));
    }

    /**
     * @param quotationRisks
     * @return
     */
    @RolesAllowed("quot_risk_save_all")
    @PostMapping("/multiple")
    public ResponseEntity<List<QuotationRisk>> saveMultiple(@RequestBody List<QuotationRisk> quotationRisks) {
        return ResponseEntity.ok(quotationRiskService.saveMultiple(quotationRisks));
    }

    /**
     * @param quotationRisk
     * @param id
     * @return
     */
    @RolesAllowed({"quot_risk_update", "agent"})
    @PutMapping("/{id}")
    public ResponseEntity<QuotationRisk> update(@RequestBody QuotationRisk quotationRisk, @PathVariable("id") Long id) {
        return ResponseEntity.ok(quotationRiskService.update(quotationRisk, id));
    }

    /**
     * @param id
     * @return
     */
    @RolesAllowed({"quot_risk_delete", "agent"})
    @DeleteMapping("/{id}")
    public ResponseEntity<QuotationRisk> delete(@PathVariable("id") Long id) {
            quotationRiskService.delete(id);
            return ResponseEntity.noContent().build();
    }

    /**
     * @param id
     * @return
     */
    @RolesAllowed("quot_risk_get_one")
    @GetMapping("/{id}")
    public ResponseEntity<QuotationRisk> find(@PathVariable("id") Long id) {
        Optional<QuotationRisk> optionalQuotationRisk = quotationRiskService.find(id);
        return optionalQuotationRisk
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());

    }

    @RolesAllowed({"quot_risk_update_cert", "quotation_service_admin"})
    @GetMapping("/certificate-no/update")
    public ResponseEntity<QuotationRisk> updateCertificateNo(@RequestParam("id") Long quotationRiskId, @RequestParam("certificate-no") String certificateNo) {
        return ResponseEntity.ok(quotationRiskService.updateCertificateNo(certificateNo, quotationRiskId));
    }

    /**
     * @param id
     * @return
     */
    @RolesAllowed("quot_risk_get_ris_sections")
    @GetMapping("/{id}/quotation-risk-sections")
    public ResponseEntity<List<QuotationRiskSection>> getQuotationRiskSections(@PathVariable("id") Long id) {
        List<QuotationRiskSection> quotationRiskSections = quotationRiskService
                .getQuotationRiskSections(id);

        return ResponseEntity.ok(quotationRiskSections);
    }

    /**
     * @param id
     * @param quotationRiskSection
     * @return
     */
    @RolesAllowed("quot_risk_create_risk_sections")
    @PostMapping("/{id}/quotation-risk-sections")
    public ResponseEntity<QuotationRiskSection> createQuotationRiskSection(@PathVariable("id") Long id, @RequestBody QuotationRiskSection quotationRiskSection) {
        QuotationRiskSection quotationRiskSections = quotationRiskService
                .createOrUpdateQuotationRiskSection(id, quotationRiskSection);

        return ResponseEntity.ok(quotationRiskSections);
    }

    @RolesAllowed("quot_risk_get_taxes")
    @GetMapping("{id}/taxes")
    public ResponseEntity<List<QuotationRiskTax>> getRiskTaxes(@PathVariable Long id, Authentication authentication) {
        return ResponseEntity.ok(quotationRiskService.findTaxes(id));
    }
}
