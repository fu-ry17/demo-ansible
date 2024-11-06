package com.turnkey.turnquest.gis.quotation.client.gis;

import com.turnkey.turnquest.gis.quotation.dto.gis.ProductDto;
import com.turnkey.turnquest.gis.quotation.dto.gis.TaxRateDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("gis-setups-service")
public interface ProductClient {

    @GetMapping("products/{id}")
    ProductDto findById(@PathVariable Long id);

    @GetMapping("products/{id}/tax-rates?organizationId={organizationId}")
    List<TaxRateDto> findProductTaxRates(
            @PathVariable("id") Long id,
            @PathVariable("organizationId") Long organizationId
    );
}
