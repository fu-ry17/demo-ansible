package com.turnkey.turnquest.gis.quotation.dto.gis;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransactionTypeDto {

    private String subClassApplicable;

    private String code;

    private String contraGeneralLedgerCode;

    private String description;

    private String generalLedgerCode;

    private String type;

    private String applicationLevel;

    private String mandatory;

    private Long organizationId;

    private String applyNewBusiness;

    private BigDecimal reOrder;

    private String renewalEndorsement;

    private String tvLvlCOde;
}
