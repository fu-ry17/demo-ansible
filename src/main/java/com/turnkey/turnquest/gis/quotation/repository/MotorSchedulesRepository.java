package com.turnkey.turnquest.gis.quotation.repository;

import com.turnkey.turnquest.gis.quotation.model.MotorSchedules;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MotorSchedulesRepository extends JpaRepository<MotorSchedules, Long> {
    /**
     * @param id
     * @param organizationId
     * @return
     */
    Optional<MotorSchedules> findByIdAndOrganizationId(Long id, Long organizationId);

    /**
     * @param organizationId
     * @return
     */
    List<MotorSchedules> findAllByOrganizationId(Long organizationId);

    /**
     * @param organizationId
     * @param quotationRiskId
     * @return
     */
    List<MotorSchedules> findAllByOrganizationIdAndQuotationRiskId(Long organizationId, Long quotationRiskId);

}
