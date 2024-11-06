package com.turnkey.turnquest.gis.quotation.client.gis;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("gis-setups-service")
public interface CertClient {

    @GetMapping("sub-class-cert/{sub-class-id}/sub-class/{covt-id}/cover-type")
     String getCertificateType(@PathVariable("sub-class-id") Long subClassId, @PathVariable("covt-id") Long coverTypeId);
}
