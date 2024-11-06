package com.turnkey.turnquest.gis.quotation.controller;

import com.turnkey.turnquest.gis.quotation.auth.TokenUtils;
import com.turnkey.turnquest.gis.quotation.model.Attachment;
import com.turnkey.turnquest.gis.quotation.service.AttachmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@RestController
@RequestMapping(value = "/deals/docs/attachments/")
public class AttachmentController {

    private final TokenUtils tokenUtils;
    private final AttachmentService attachmentService;

    public AttachmentController(TokenUtils tokenUtils,AttachmentService attachmentService){
        this.tokenUtils = tokenUtils;
        this.attachmentService = attachmentService;
    }


    @RolesAllowed({"quot_att_get_all", "agent"})
    @GetMapping
    public ResponseEntity<List<Attachment>> getAllAttachments(Authentication authentication){
        tokenUtils.init(authentication);
        long organizationId = tokenUtils.getOrganizationId();
        return ResponseEntity.ok(attachmentService.findAllAttachments(organizationId));
    }

    @RolesAllowed("quot_att_get")
    @GetMapping("/{id}")
    public ResponseEntity<Attachment> getOneAttachmentById(@PathVariable long id, Authentication authentication){
        tokenUtils.init(authentication);
        long organizationId = tokenUtils.getOrganizationId();
        return ResponseEntity.ok(attachmentService.findAttachment(id,organizationId));
    }


    @RolesAllowed("quot_att_save")
    @PostMapping
    public ResponseEntity<Attachment>  saveAttachment(@RequestBody Attachment attachment,Authentication authentication){
        tokenUtils.init(authentication);
        long organizationId = tokenUtils.getOrganizationId();
        return ResponseEntity.ok(attachmentService.saveAttachment(attachment,organizationId));
    }

    @RolesAllowed("quot_att_save_all")
    @PostMapping("/all")
    public ResponseEntity<List<Attachment>>  saveAllAttachments(@RequestBody List<Attachment> attachments, Authentication authentication){
        tokenUtils.init(authentication);
        long organizationId = tokenUtils.getOrganizationId();
        return ResponseEntity.ok(attachmentService.saveAllAttachments(attachments,organizationId));
    }


    @RolesAllowed("quot_att_delete")
    @DeleteMapping
    public ResponseEntity<Attachment> deleteAttachment(@RequestBody Attachment attachment){
        attachmentService.deleteAttachment(attachment);
        return ResponseEntity.noContent().build();
    }

}
