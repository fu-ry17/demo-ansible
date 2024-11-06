package com.turnkey.turnquest.gis.quotation.client.endorsement;

import com.turnkey.turnquest.gis.quotation.dto.endorsement.PolicyActiveRiskDto;
import com.turnkey.turnquest.gis.quotation.model.Quotation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "endorsement-service")
public interface EndorsementClient {

    @PostMapping("/policy-active-risk/save-all-active-risk")
    List<PolicyActiveRiskDto> saveAllActiveRisk(@RequestBody List<PolicyActiveRiskDto> policyActiveRisk);

    @GetMapping("/risk-installments/compute-quote-first-installment")
    Quotation computeQuoteFirstInstallment(@RequestParam String quotationNumber);

    @GetMapping("/policy-active-risk/find-active-risks-as-quote")
    Quotation findActiveRiskByQuotationNumber(@RequestParam String quotationNumber);

    @PostMapping("/policy-active-risk/save-quotation-annual-risk")
    Quotation saveAnnualActiveRisk(@RequestBody List<PolicyActiveRiskDto> policyActiveRisk);
}
