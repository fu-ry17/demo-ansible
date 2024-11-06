package com.turnkey.turnquest.gis.quotation.client.underwriting;

import com.turnkey.turnquest.gis.quotation.dto.underwriting.PolicyDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("gis-underwriting-service")
public interface PolicyClientMock {

    @GetMapping("policies/batch-number/{batchNo}")
    PolicyDto findPolicyByBatchNumber(@PathVariable Long batchNo);
}
