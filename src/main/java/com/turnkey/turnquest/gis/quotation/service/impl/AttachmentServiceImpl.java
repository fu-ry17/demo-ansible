package com.turnkey.turnquest.gis.quotation.service.impl;


import com.turnkey.turnquest.gis.quotation.model.Attachment;
import com.turnkey.turnquest.gis.quotation.repository.AttachmentRepository;
import com.turnkey.turnquest.gis.quotation.service.AttachmentService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service("attachmentService")
public class AttachmentServiceImpl implements AttachmentService {

    private final AttachmentRepository attachmentRepository;

    public AttachmentServiceImpl(AttachmentRepository attachmentRepository) {
        this.attachmentRepository = attachmentRepository;
    }

    @Override
    public List<Attachment> findAllAttachments(long organizationId) {
        return attachmentRepository.findAllByOrganizationId(organizationId);
    }

    @Override
    public Attachment findAttachment(long id, long organizationId) {
        return attachmentRepository.findByIdAndOrganizationId(id, organizationId);
    }

    @Override
    public Attachment saveAttachment(Attachment attachment, long organizationId) {
        if (attachment.getOrganizationId() == -1L) {
            attachment.setOrganizationId(organizationId);
        }

        return attachmentRepository.save(attachment);
    }

    @Override
    public List<Attachment> saveAllAttachments(List<Attachment> attachments, long organizationId) {
        return attachments.stream()
                .peek(a -> a.setOrganizationId(organizationId))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteAttachment(Attachment attachment) {
        attachmentRepository.delete(attachment);
    }

}
