package com.turnkey.turnquest.gis.quotation.dto.deals;

import com.turnkey.turnquest.gis.quotation.enums.DealStatus;
import com.turnkey.turnquest.gis.quotation.enums.OverAllStatus;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class DealDto {

    private Long id;

    private String description;

    private BigDecimal amount;

    private Long timeStamp;

    private String dealSource;

    private Long organizationId;

    private Long clientId;

    private Long insurerOrgId;

    private Long quotationId;

    private Long activeDate;

    private DealStatus dealStatus;

    private OverAllStatus overAllStatus;

    private Long assignedTo;

    private String reasonWhyClosed;
}
