package com.turnkey.turnquest.gis.quotation.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.turnkey.turnquest.gis.quotation.model.audit.BaseAudit;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "GIN_MOTOR_PRIVATE_SCH")
@JsonIgnoreProperties(value = {"quotationRisk","quotationRisks","motorSchedules"},ignoreUnknown = true)
public class MotorSchedules extends BaseAudit implements Serializable {

    private static final long serialVersionUID = -5225689654137697876L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gin-motor-private-sch-seq")
    @SequenceGenerator(name="gin-motor-private-sch-seq", sequenceName = "GIN_MOTOR_PRIVATE_SCH_SEQ", allocationSize = 1)
    @Column(name = "MPS_IPU_ID")
    private Long id;

    @Column(name = "MPS_IPU_CODE")
    private Long ipuCode;

    @JsonBackReference
    @OneToOne(mappedBy = "motorSchedules")
    private QuotationRisk quotationRisk;

    @Column(name = "MPS_REG_NO")
    private String riskId;

    @Column(name = "MPS_MAKE")
    private String make;

    @Column(name = "MPS_CUBIC_CAPACITY")
    private Long cubicCapacity;

    @Column(name = "MPS_YR_MANFT")
    private Long yearOfManufacture;

    @Column(name = "MPS_YR_REG")
    private Long yearOfRegistration;

    @Column(name = "MPS_CARRY_CAPACITY")
    private Long carryCapacity;

    @Column(name = "MPS_VALUE")
    private BigDecimal value;

    @Column(name = "MPS_BODY_TYPE")
    private String bodyType;

    @Column(name = "MPS_CHASIS_NO")
    private String chasisNo;

    @Column(name = "MPS_ENGINE_NO")
    private String engineNo;

    @Column(name = "MPS_COLOR")
    private String color;

    @Column(name = "LOGBOOK")
    private String logbook;

    @Column(name = "MPS_TONNAGE")
    private String tonnage;

    @Column(name = "NOPRINT")
    private int noPrint;

    @Column(name = "CERTNO")
    private Long certificateNo;

    @Column(name = "CERT_TYPE")
    private String certificateType;

    @Column(name = "MODEL")
    private String model;

    @Column(name = "MPS_ORG_ID")
    private Long organizationId;
}
