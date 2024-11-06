package com.turnkey.turnquest.gis.quotation.service;


import com.turnkey.turnquest.gis.quotation.model.Attachment;

import java.util.List;

public interface AttachmentService {

    List<Attachment> findAllAttachments(long organizationId);

    Attachment findAttachment(long id,long organizationId);

    Attachment saveAttachment(Attachment attachment,long organizationId);

    List<Attachment> saveAllAttachments(List<Attachment> attachments,long organizationId);

    void deleteAttachment(Attachment attachment);
}

