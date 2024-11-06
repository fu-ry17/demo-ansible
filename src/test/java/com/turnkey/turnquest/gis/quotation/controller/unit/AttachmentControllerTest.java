package com.turnkey.turnquest.gis.quotation.controller.unit;

import com.turnkey.turnquest.gis.quotation.auth.TokenUtils;
import com.turnkey.turnquest.gis.quotation.controller.AttachmentController;
import com.turnkey.turnquest.gis.quotation.model.Attachment;
import com.turnkey.turnquest.gis.quotation.service.AttachmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author Titus Murithi Bundi
 */
class AttachmentControllerTest {

    @Mock
    private AttachmentService attachmentService;

    @Mock
    private TokenUtils tokenUtils;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private AttachmentController attachmentController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnAllAttachments() {
        // Given
        Attachment attachment1 = new Attachment();
        Attachment attachment2 = new Attachment();
        List<Attachment> attachments = Arrays.asList(attachment1, attachment2);

        when(tokenUtils.getOrganizationId()).thenReturn(1L);
        when(attachmentService.findAllAttachments(1L)).thenReturn(attachments);

        // When
        ResponseEntity<List<Attachment>> response = attachmentController.getAllAttachments(authentication);

        // Then
        assertEquals(attachments, response.getBody());
    }

    @Test
    void shouldReturnAttachmentById() {
        // Given
        Attachment attachment = new Attachment();

        when(tokenUtils.getOrganizationId()).thenReturn(1L);
        when(attachmentService.findAttachment(1L, 1L)).thenReturn(attachment);

        // When
        ResponseEntity<Attachment> response = attachmentController.getOneAttachmentById(1L, authentication);

        // Then
        assertEquals(attachment, response.getBody());
    }

    @Test
    void shouldSaveAttachment() {
        // Given
        Attachment attachment = new Attachment();

        when(tokenUtils.getOrganizationId()).thenReturn(1L);
        when(attachmentService.saveAttachment(attachment, 1L)).thenReturn(attachment);

        // When
        ResponseEntity<Attachment> response = attachmentController.saveAttachment(attachment, authentication);

        // Then
        assertEquals(attachment, response.getBody());
    }

    @Test
    void shouldDeleteAttachment() {
        // Given
        Attachment attachment = new Attachment();

        // When
        ResponseEntity<Attachment> response = attachmentController.deleteAttachment(attachment);

        // Then
        verify(attachmentService, times(1)).deleteAttachment(attachment);
        assertEquals(204, response.getStatusCodeValue());
    }
}
