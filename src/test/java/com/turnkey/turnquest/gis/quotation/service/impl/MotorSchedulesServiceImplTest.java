package com.turnkey.turnquest.gis.quotation.service.impl;

import com.turnkey.turnquest.gis.quotation.model.MotorSchedules;
import com.turnkey.turnquest.gis.quotation.repository.MotorSchedulesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class MotorSchedulesServiceImplTest {

    @Mock
    private MotorSchedulesRepository motorSchedulesRepository;

    private MotorSchedulesServiceImpl motorSchedulesService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        motorSchedulesService = new MotorSchedulesServiceImpl(motorSchedulesRepository);
    }

    @Test
    void shouldFindMotorScheduleByIdAndOrganizationId() throws Exception {
        MotorSchedules motorSchedules = new MotorSchedules();
        when(motorSchedulesRepository.findByIdAndOrganizationId(1L, 1L)).thenReturn(Optional.of(motorSchedules));

        Optional<MotorSchedules> result = motorSchedulesService.findById(1L, 1L);

        assertEquals(Optional.of(motorSchedules), result);
    }

    @Test
    void shouldFindAllMotorSchedulesByOrganizationId() {
        MotorSchedules motorSchedules1 = new MotorSchedules();
        MotorSchedules motorSchedules2 = new MotorSchedules();
        List<MotorSchedules> motorSchedules = Arrays.asList(motorSchedules1, motorSchedules2);
        when(motorSchedulesRepository.findAllByOrganizationId(1L)).thenReturn(motorSchedules);

        List<MotorSchedules> result = motorSchedulesService.findAll(1L);

        assertEquals(2, result.size());
    }

    @Test
    void shouldSaveMotorScheduleWithOrganizationId() throws Exception {
        MotorSchedules motorSchedules = new MotorSchedules();
        when(motorSchedulesRepository.save(motorSchedules)).thenReturn(motorSchedules);

        MotorSchedules result = motorSchedulesService.save(motorSchedules, 1L);

        assertEquals(1L, result.getOrganizationId());
    }

    @Test
    void shouldFindMotorSchedulesByQuotationRiskIdAndOrganizationId() {
        MotorSchedules motorSchedules1 = new MotorSchedules();
        MotorSchedules motorSchedules2 = new MotorSchedules();
        List<MotorSchedules> motorSchedules = Arrays.asList(motorSchedules1, motorSchedules2);
        when(motorSchedulesRepository.findAllByOrganizationIdAndQuotationRiskId(1L, 1L)).thenReturn(motorSchedules);

        List<MotorSchedules> result = motorSchedulesService.findByQuotationRiskId(1L, 1L);

        assertEquals(2, result.size());
    }
}
