package com.turnkey.turnquest.gis.quotation.aki.client;

import com.turnkey.turnquest.gis.quotation.aki.dto.CertificateRequestDto;
import com.turnkey.turnquest.gis.quotation.aki.dto.CertificateResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("dmvic-service")
public interface DMVICService {

    @PostMapping("/aki/validate")
    CertificateResponseDto validateRisk(@RequestBody CertificateRequestDto certificateRequestDto);

}
