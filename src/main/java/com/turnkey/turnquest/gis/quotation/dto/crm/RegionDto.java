package com.turnkey.turnquest.gis.quotation.dto.crm;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class RegionDto implements Serializable {

    private static final long serialVersionUID = 593678862458567099L;

    private Long id;

    private OrganizationDto organization;

    private String code;

    private String name;

    private Date dateFrom;

    private Date dateTo;

    private AgentDto agent;

    private String branchMgrSeqNo;

    private String agentSeqNo;

    private Long policySeqNo;

    private Long clientSequence;

    private Long preContractAgentSeqNo;

    private String computeOverOwnBusiness;
}
