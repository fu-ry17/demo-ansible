package com.turnkey.turnquest.gis.quotation.model;


import com.turnkey.turnquest.gis.quotation.dto.crm.ClientDto;
import com.turnkey.turnquest.gis.quotation.dto.crm.OrganizationDto;
import com.turnkey.turnquest.gis.quotation.dto.todo.TodoDto;
import com.turnkey.turnquest.gis.quotation.enums.DealStatus;
import com.turnkey.turnquest.gis.quotation.enums.OverAllStatus;
import com.turnkey.turnquest.gis.quotation.model.audit.BaseAudit;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class Deal extends BaseAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "deal-seq")
    @SequenceGenerator(name="deal-seq", sequenceName = "DEAL_SEQ", allocationSize = 1)
    private Long id;

    @Lob
    private String description;

    private BigDecimal amount;

    private Long timeStamp;

    private String dealSource;

    private Long organizationId;

    private Long clientId;

    private Long insurerOrgId;

    private Long quotationId;

    private Long activeDate;

    private DealStatus dealStatus = DealStatus.PENDING;

    private OverAllStatus overAllStatus = OverAllStatus.OPEN;

    private Long assignedTo;

    @Lob
    private String reasonWhyClosed;

    @Transient
    private List<TodoDto> todo;

    @Transient
    private ClientDto client;

    @Transient
    private Quotation quotation;

    @Transient
    private OrganizationDto organization;

}
