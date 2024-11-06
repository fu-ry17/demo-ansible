package com.turnkey.turnquest.gis.quotation.controller;

import com.turnkey.turnquest.gis.quotation.auth.TokenUtils;
import com.turnkey.turnquest.gis.quotation.model.MotorSchedules;
import com.turnkey.turnquest.gis.quotation.service.MotorSchedulesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping(value = "/motor-schedule")
public class MotorScheduleController {

    private final MotorSchedulesService motorSchedulesService;
    private final TokenUtils tokenUtils;

    @Autowired
    public MotorScheduleController(MotorSchedulesService motorSchedulesService, TokenUtils tokenUtils) {
        this.motorSchedulesService = motorSchedulesService;
        this.tokenUtils = tokenUtils;
    }

    @RolesAllowed("quot_motor-sch_get_all")
    @GetMapping
    public ResponseEntity<List<MotorSchedules>> getAllMotorSchedules(Authentication authentication) {
        tokenUtils.init(authentication);
        return ResponseEntity.ok(motorSchedulesService.findAll(tokenUtils.getOrganizationId()));
    }

    @RolesAllowed("quot_motor-sch_get_one")
    @GetMapping("/{id}")
    public ResponseEntity<MotorSchedules> find(@PathVariable("id") Long id, Authentication authentication) throws Exception {
        tokenUtils.init(authentication);
        Optional<MotorSchedules> optionalQuotation = motorSchedulesService.findById(id, tokenUtils.getOrganizationId());
        return optionalQuotation
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }

    @RolesAllowed("quot_motor-sch_add")
    @PostMapping("/save")
    public ResponseEntity<MotorSchedules> add(@RequestBody MotorSchedules motorSchedules, Authentication authentication) throws Exception {
        tokenUtils.init(authentication);
        return ResponseEntity.ok(motorSchedulesService.save(motorSchedules, tokenUtils.getOrganizationId()));
    }

    @RolesAllowed("quot_motor-sch_get_by_risk_id")
    @GetMapping("find-by/{quotationRiskId}/quotation-risk-id")
    public ResponseEntity<List<MotorSchedules>> findByQuotationRiskId(@PathVariable Long quotationRiskId, Authentication authentication) {
        tokenUtils.init(authentication);
        return ResponseEntity.ok(motorSchedulesService.findByQuotationRiskId(quotationRiskId, tokenUtils.getOrganizationId()));
    }

}
