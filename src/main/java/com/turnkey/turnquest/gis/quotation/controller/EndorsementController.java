package com.turnkey.turnquest.gis.quotation.controller;

import com.turnkey.turnquest.gis.quotation.enums.YesNo;
import com.turnkey.turnquest.gis.quotation.model.Quotation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.turnkey.turnquest.gis.quotation.service.EndorsementService;

@RestController
@RequestMapping("endorsements")
public class EndorsementController {
    private final EndorsementService endorsementService;

    public EndorsementController(EndorsementService endorsementService) {
        this.endorsementService = endorsementService;
    }

    @PostMapping("annual")
    public ResponseEntity<Quotation> saveAnnualQuotationRisk(@RequestBody Quotation quotation) {
        return ResponseEntity.ok(endorsementService.saveAnnualQuotationRisk(quotation, YesNo.Y));
    }

    @PostMapping("first-installment")
    public ResponseEntity<Quotation> computeFirstInstallment(@RequestBody Quotation quotation) {
        return ResponseEntity.ok(endorsementService.computeFirstInstallment(quotation));
    }

    @GetMapping("next-installment")
    public ResponseEntity<Quotation> computeNextInstallment(@RequestParam String quotationNumber) {
        return ResponseEntity.ok(endorsementService.getQuotationToPay(quotationNumber));
    }
}
