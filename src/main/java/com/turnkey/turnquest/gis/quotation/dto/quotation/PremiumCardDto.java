package com.turnkey.turnquest.gis.quotation.dto.quotation;


import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Titus Murithi Bundi
 */

@Data
public class PremiumCardDto {

     Long clientId;

     String paymentRef;

     String quotationNo;

     BigDecimal basicPremium = BigDecimal.ZERO;

     BigDecimal totalPremium = BigDecimal.ZERO;

     BigDecimal commission = BigDecimal.ZERO;

     BigDecimal withHoldingTax = BigDecimal.ZERO;

     BigDecimal riskTaxes = BigDecimal.ZERO;

     BigDecimal policyTaxes = BigDecimal.ZERO;

     String paymentFrequency = "A";

     BigDecimal fees = BigDecimal.ZERO;

     BigDecimal incentives = BigDecimal.ZERO;

     Long quotationId;

     Long organizationId;




}
