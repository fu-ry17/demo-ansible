package com.turnkey.turnquest.gis.quotation.service.impl;
import com.turnkey.turnquest.gis.quotation.model.Attachment;
import com.turnkey.turnquest.gis.quotation.repository.AttachmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class AttachmentServiceImplTest {

    @Mock
    private AttachmentRepository attachmentRepository;

    private AttachmentServiceImpl attachmentService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        attachmentService = new AttachmentServiceImpl(attachmentRepository);
    }

    @Test
    void shouldFindAllAttachmentsByOrganizationId() {
        Attachment attachment1 = new Attachment();
        Attachment attachment2 = new Attachment();
        List<Attachment> attachments = Arrays.asList(attachment1, attachment2);
        when(attachmentRepository.findAllByOrganizationId(1L)).thenReturn(attachments);

        List<Attachment> result = attachmentService.findAllAttachments(1L);

        assertEquals(2, result.size());
        verify(attachmentRepository, times(1)).findAllByOrganizationId(1L);
    }

    @Test
    void shouldFindAttachmentByIdAndOrganizationId() {
        Attachment attachment = new Attachment();
        when(attachmentRepository.findByIdAndOrganizationId(1L, 1L)).thenReturn(attachment);

        Attachment result = attachmentService.findAttachment(1L, 1L);

        assertEquals(attachment, result);
        verify(attachmentRepository, times(1)).findByIdAndOrganizationId(1L, 1L);
    }

    @Test
    void shouldSaveAttachmentWithOrganizationId() {
        Attachment attachment = new Attachment();
        attachment.setOrganizationId(-1L);
        when(attachmentRepository.save(attachment)).thenReturn(attachment);

        Attachment result = attachmentService.saveAttachment(attachment, 1L);

        assertEquals(1L, result.getOrganizationId());
        verify(attachmentRepository, times(1)).save(attachment);
    }

    @Test
    void shouldSaveAllAttachmentsWithOrganizationId() {
        Attachment attachment1 = new Attachment();
        Attachment attachment2 = new Attachment();
        List<Attachment> attachments = Arrays.asList(attachment1, attachment2);
        when(attachmentRepository.saveAll(attachments)).thenReturn(attachments);

        List<Attachment> result = attachmentService.saveAllAttachments(attachments, 1L);

        assertEquals(2, result.size());
        result.forEach(attachment -> assertEquals(1L, attachment.getOrganizationId()));
    }

    @Test
    void shouldDeleteAttachment() {
        Attachment attachment = new Attachment();

        attachmentService.deleteAttachment(attachment);

        verify(attachmentRepository, times(1)).delete(attachment);
    }
}