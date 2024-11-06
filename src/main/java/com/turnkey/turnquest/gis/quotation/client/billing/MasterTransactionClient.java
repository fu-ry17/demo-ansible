package com.turnkey.turnquest.gis.quotation.client.billing;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;

@FeignClient("billing-service")
public interface MasterTransactionClient {

    @GetMapping("/v1/master-transactions/{payRef}/outstanding?debitAmount={debitAmount}")
    BigDecimal getOutstandingAmount(@PathVariable String payRef,
                                    @PathVariable BigDecimal debitAmount);
}
