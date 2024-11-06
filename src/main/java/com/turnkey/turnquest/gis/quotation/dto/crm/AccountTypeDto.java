/**
 * 2018-07-18
 */
package com.turnkey.turnquest.gis.quotation.dto.crm;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author Kevin Kibet
 *
 */
@Data
public class AccountTypeDto implements Serializable {

    private Long id;

    private String code;

    private String accountType;

    private String typeId;

    private BigDecimal withHoldingTaxRate;

    private Long commissionReservedRate;

    private BigDecimal maxAdvanceAmount;

    private BigDecimal maxAdvanceRepaymentAmount;

    private String receiptsIncludeCommission;

    private Long overrideRate;

    private String idSerialFormat;

    private Long vatRate;

    private String format;

    private String odlCode;

    private String noGenCode;

    private String typeShortDescriptionBkp;

    private Long commLevyRate;

    private Long exciseDutyRate;

    private Long commissionLevyRate;

    private String creditPolicyApplicable;

    private Long ltaWithholdingTaxRate;

    private Long nextNo;

    private String suffix;

}
