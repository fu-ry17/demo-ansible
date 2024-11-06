package com.turnkey.turnquest.gis.quotation.dto.billing;

import com.turnkey.turnquest.gis.quotation.enums.ValuationStatus;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PremiumCardDto {

     Long id;

     BigDecimal outstandingBalance;

     BigDecimal outstandingCommission;

     Long maturityDate;

     BigDecimal totalPaidInstalments;

     BigDecimal unallocatedAmount;

     BigDecimal unallocatedCommAmount;

     ValuationStatus valuationStatus;

     String paymentRef;

     Long organizationId;
}
