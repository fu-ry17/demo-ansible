package com.turnkey.turnquest.gis.quotation.controller.unit;

import com.turnkey.turnquest.gis.quotation.auth.TokenUtils;
import com.turnkey.turnquest.gis.quotation.controller.MotorScheduleController;
import com.turnkey.turnquest.gis.quotation.model.MotorSchedules;
import com.turnkey.turnquest.gis.quotation.service.MotorSchedulesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * @author Titus Murithi Bundi
 */
class MotorScheduleControllerTest {

    @Mock
    private MotorSchedulesService motorSchedulesService;

    @Mock
    private TokenUtils tokenUtils;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private MotorScheduleController motorScheduleController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnAllMotorSchedules() {
        // Given
        MotorSchedules motorSchedule1 = new MotorSchedules();
        MotorSchedules motorSchedule2 = new MotorSchedules();
        List<MotorSchedules> motorSchedules = Arrays.asList(motorSchedule1, motorSchedule2);

        when(tokenUtils.getOrganizationId()).thenReturn(1L);
        when(motorSchedulesService.findAll(1L)).thenReturn(motorSchedules);

        // When
        ResponseEntity<List<MotorSchedules>> response = motorScheduleController.getAllMotorSchedules(authentication);

        // Then
        assertEquals(motorSchedules, response.getBody());
    }

    @Test
    void shouldReturnMotorScheduleById() throws Exception {
        // Given
        MotorSchedules motorSchedule = new MotorSchedules();

        when(tokenUtils.getOrganizationId()).thenReturn(1L);
        when(motorSchedulesService.findById(1L, 1L)).thenReturn(Optional.of(motorSchedule));

        // When
        ResponseEntity<MotorSchedules> response = motorScheduleController.find(1L, authentication);

        // Then
        assertEquals(motorSchedule, response.getBody());
    }

    @Test
    void shouldReturnNoContentWhenMotorScheduleNotFound() throws Exception {
        // Given
        when(tokenUtils.getOrganizationId()).thenReturn(1L);
        when(motorSchedulesService.findById(1L, 1L)).thenReturn(Optional.empty());

        // When
        ResponseEntity<MotorSchedules> response = motorScheduleController.find(1L, authentication);

        // Then
        assertEquals(204, response.getStatusCodeValue());
    }

    @Test
    void shouldSaveMotorSchedule() throws Exception {
        // Given
        MotorSchedules motorSchedule = new MotorSchedules();

        when(tokenUtils.getOrganizationId()).thenReturn(1L);
        when(motorSchedulesService.save(motorSchedule, 1L)).thenReturn(motorSchedule);

        // When
        ResponseEntity<MotorSchedules> response = motorScheduleController.add(motorSchedule, authentication);

        // Then
        assertEquals(motorSchedule, response.getBody());
    }

    @Test
    void shouldReturnMotorSchedulesByQuotationRiskId() {
        // Given
        MotorSchedules motorSchedule1 = new MotorSchedules();
        MotorSchedules motorSchedule2 = new MotorSchedules();
        List<MotorSchedules> motorSchedules = Arrays.asList(motorSchedule1, motorSchedule2);

        when(tokenUtils.getOrganizationId()).thenReturn(1L);
        when(motorSchedulesService.findByQuotationRiskId(1L, 1L)).thenReturn(motorSchedules);

        // When
        ResponseEntity<List<MotorSchedules>> response = motorScheduleController.findByQuotationRiskId(1L, authentication);

        // Then
        assertEquals(motorSchedules, response.getBody());
    }

}
