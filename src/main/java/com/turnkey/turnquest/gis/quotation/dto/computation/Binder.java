package com.turnkey.turnquest.gis.quotation.dto.computation;

import lombok.Data;

import java.util.List;

@Data
public class Binder {
    private Long id;
    private String name;
    private String type;
    private Long productId;
    private Long productGroupId;
    private Long agentId;
    private Long panelId;
    private Long currencyId;
    private Long organizationId;
    private String commissionType;
    private List<String> agentCommissions;
    private Organization organization;
    private String premiumRates;

}
