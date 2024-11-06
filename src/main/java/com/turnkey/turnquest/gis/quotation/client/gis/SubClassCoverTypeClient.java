package com.turnkey.turnquest.gis.quotation.client.gis;

import com.turnkey.turnquest.gis.quotation.enums.YesNo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("gis-setups-service")
public interface SubClassCoverTypeClient {

    @GetMapping("sub-class-cover-types/find-valuation-allowed/{productSubClass}/product-sub-class/{coverTypeId}/cover-type")
    YesNo requiresValuation(
            @PathVariable  Long productSubClass,
            @PathVariable  Long coverTypeId
    );

}
