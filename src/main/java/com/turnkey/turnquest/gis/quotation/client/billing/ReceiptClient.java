package com.turnkey.turnquest.gis.quotation.client.billing;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@FeignClient("billing-service")
public interface ReceiptClient {

    @GetMapping("/receipts/generate-debit")
    void generateDebitReport(@RequestParam String iPayRef,
                             @RequestParam Long policyNo,
                             @RequestParam Long quotationId);

    @GetMapping("/receipts/quotation/outstanding")
    BigDecimal getTotalAmountPaidByQuotationNo(@RequestParam String quoteNo);
}
