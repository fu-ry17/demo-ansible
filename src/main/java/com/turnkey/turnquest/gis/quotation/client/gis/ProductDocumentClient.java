package com.turnkey.turnquest.gis.quotation.client.gis;

import com.turnkey.turnquest.gis.quotation.dto.gis.ProductDocumentDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient("gis-setups-service")
public interface ProductDocumentClient {

    @GetMapping("product-documents/by-id/{productId}")
    List<ProductDocumentDto> getByProductId(@PathVariable Long productId);
}
