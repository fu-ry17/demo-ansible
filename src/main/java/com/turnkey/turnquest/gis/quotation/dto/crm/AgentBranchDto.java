package com.turnkey.turnquest.gis.quotation.dto.crm;

import lombok.Data;

@Data
public class AgentBranchDto {
    private Long id;

    private Long organizationId;

    private Long branchId;

    private Long insurer;

    private BranchDto branch;
}
