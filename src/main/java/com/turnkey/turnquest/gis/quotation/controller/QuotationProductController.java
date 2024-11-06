package com.turnkey.turnquest.gis.quotation.controller;

import com.turnkey.turnquest.gis.quotation.dto.quotation.UpdateInstallmentDto;
import com.turnkey.turnquest.gis.quotation.enums.YesNo;
import com.turnkey.turnquest.gis.quotation.model.QuotationProduct;
import com.turnkey.turnquest.gis.quotation.model.QuotationRisk;
import com.turnkey.turnquest.gis.quotation.service.QuotationProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;
import java.util.Optional;


@CrossOrigin
@RestController
@RequestMapping(value = "/quotation-products")
public class QuotationProductController {

    private final QuotationProductService quotationProductService;

    @Autowired
    public QuotationProductController(QuotationProductService quotationProductService) {
        this.quotationProductService = quotationProductService;
    }

    @RolesAllowed("quot_product_create")
    @PostMapping
    public ResponseEntity<QuotationProduct> create(@RequestBody QuotationProduct quotationProduct) {
        quotationProduct = quotationProductService.create(quotationProduct);

        return ResponseEntity.ok(quotationProduct);
    }

    @RolesAllowed("quot_product_update")
    @PutMapping("/{id}")
    public ResponseEntity<QuotationProduct> update(@RequestBody QuotationProduct quotationProduct, @PathVariable("id") Long id) {
        quotationProduct = quotationProductService.update(quotationProduct, id);

        return ResponseEntity.ok(quotationProduct);
    }

    @RolesAllowed({"quot_product_get_one","agent"})
    @GetMapping("/{id}")
    public ResponseEntity<QuotationProduct> find(@PathVariable("id") Long id) {
        Optional<QuotationProduct> optionalQuotationProduct = quotationProductService.find(id);

        return optionalQuotationProduct.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @RolesAllowed({"quot_product_get_risks","agent"})
    @GetMapping("/{id}/quotation-risks")
    public ResponseEntity<List<QuotationRisk>> getQuotationRisks(@PathVariable("id") Long id) {
        List<QuotationRisk> quotationRisks = quotationProductService.getQuotationRisks(id);

        return ResponseEntity.ok(quotationRisks);
    }

    @RolesAllowed("quot_product_convert")
    @PostMapping("/{id}/convert")
    public ResponseEntity<String> convertToPolicy(@PathVariable("id") Long id) throws Exception {
        quotationProductService.convertToPolicy(id);

        return ResponseEntity.ok("Conversion started successfully");
    }

    @RolesAllowed({"quot_product_update_inst","agent","quotation_service_admin"})
    @PostMapping("/installment-amount/update")
    public ResponseEntity<QuotationProduct> updateInstallmentAmounts(@RequestBody UpdateInstallmentDto updateInstallmentDto){
        return ResponseEntity.ok(quotationProductService.updateInstallmentAmounts(updateInstallmentDto));
    }

    @RolesAllowed({"quot_product_change_pay_opt","agent","quotation_service_admin"})
    @GetMapping("change-payment-option/{qpId}/{installmentAllowed}")
    public ResponseEntity<QuotationProduct> changePaymentOption(@PathVariable("qpId") Long quotationProductId,
                                                                @PathVariable("installmentAllowed") YesNo installmentAllowed){
        return ResponseEntity.ok(quotationProductService.changePaymentOption(quotationProductId,installmentAllowed));
    }

}
