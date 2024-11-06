package com.turnkey.turnquest.gis.quotation.service;

import com.turnkey.turnquest.gis.quotation.model.MotorSchedules;

import java.util.List;
import java.util.Optional;

public interface MotorSchedulesService {

    Optional<MotorSchedules> findById(Long id, Long organizationId) throws Exception;

    List<MotorSchedules> findAll(Long organizationId);

    MotorSchedules save(MotorSchedules motorSchedule, Long organizationId) throws Exception;

    List<MotorSchedules> findByQuotationRiskId(Long quotationRiskId, Long organizationId);

}
