package com.turnkey.turnquest.gis.quotation.client.DocsService;

import com.turnkey.turnquest.gis.quotation.dto.document.S3Object;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("document-manager-service")
public interface DocsServiceClient {
    @PostMapping("files/reports")
    List<S3Object> saveAttachment(@RequestBody MultiValueMap<String, Object> file,
                                  @RequestParam("insurerOrgId") Long insurerOrgId,
                                  @RequestParam("category") String category,
                                  @RequestParam("clientId") Long clientId,
                                  @RequestParam("policyNo") String policyNo);
}
