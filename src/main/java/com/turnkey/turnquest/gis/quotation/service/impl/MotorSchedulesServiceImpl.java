package com.turnkey.turnquest.gis.quotation.service.impl;

import com.turnkey.turnquest.gis.quotation.model.MotorSchedules;
import com.turnkey.turnquest.gis.quotation.repository.MotorSchedulesRepository;
import com.turnkey.turnquest.gis.quotation.service.MotorSchedulesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class MotorSchedulesServiceImpl implements MotorSchedulesService {

    private final Logger LOGGER = LoggerFactory.getLogger(MotorSchedulesServiceImpl.class);

    public final MotorSchedulesRepository motorSchedulesRepository;


    @Autowired
    public MotorSchedulesServiceImpl(MotorSchedulesRepository motorSchedulesRepository) {
        this.motorSchedulesRepository = motorSchedulesRepository;
    }

    @Override
    public Optional<MotorSchedules> findById(Long id, Long organizationId) throws Exception {
        return motorSchedulesRepository.findByIdAndOrganizationId(id, organizationId);
    }

    @Override
    public List<MotorSchedules> findAll(Long organizationId) {
        return motorSchedulesRepository.findAllByOrganizationId(organizationId);
    }

    @Override
    public MotorSchedules save(MotorSchedules motorSchedules, Long organizationId) throws Exception {
        motorSchedules.setOrganizationId(organizationId);
        return motorSchedulesRepository.save(motorSchedules);
    }

    @Override
    public List<MotorSchedules> findByQuotationRiskId(Long quotationRiskId, Long organizationId) {
        return motorSchedulesRepository.findAllByOrganizationIdAndQuotationRiskId(organizationId, quotationRiskId);
    }


}
