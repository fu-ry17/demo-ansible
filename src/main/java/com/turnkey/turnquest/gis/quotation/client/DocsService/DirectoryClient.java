package com.turnkey.turnquest.gis.quotation.client.DocsService;

import com.turnkey.turnquest.gis.quotation.dto.document.AttachmentDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("document-manager-service")
public interface DirectoryClient {
    @RequestMapping(method = RequestMethod.POST, value = "/files/image", consumes = MediaType.APPLICATION_JSON_VALUE)
    AttachmentDto saveImage(@RequestBody AttachmentDto attachmentDto);
}
