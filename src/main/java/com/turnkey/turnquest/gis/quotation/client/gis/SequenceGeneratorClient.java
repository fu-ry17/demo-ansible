package com.turnkey.turnquest.gis.quotation.client.gis;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient("gis-setups-service")
public interface SequenceGeneratorClient {

    @PostMapping("quotation/sequence")
    String generateQuotationNumber(@RequestBody Map<String, String> quotationAttributes);

    @PostMapping("renewal/sequence")
    String generateRenewalNumber(@RequestBody Map<String, String> quotationAttributes);
}
