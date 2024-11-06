package com.turnkey.turnquest.gis.quotation.client.certificate;

import com.turnkey.turnquest.gis.quotation.dto.certificate.VehicleSearchRequestDto;
import com.turnkey.turnquest.gis.quotation.dto.certificate.VehicleSearchResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("dmvic-service")
public interface CertificateClient {

    @PostMapping("aki/vehicle-details")
    VehicleSearchResponseDto vehicleDetails(@RequestBody VehicleSearchRequestDto vehicleSearchRequestDto);
}
