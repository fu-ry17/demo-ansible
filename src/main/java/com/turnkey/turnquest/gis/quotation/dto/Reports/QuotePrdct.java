package com.turnkey.turnquest.gis.quotation.dto.Reports;

import com.turnkey.turnquest.gis.quotation.enums.YesNo;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class QuotePrdct {

    private Long productId;

    private BigDecimal totalSumInsured= BigDecimal.ZERO;

    private BigDecimal totalPremium = BigDecimal.ZERO;

    private BigDecimal futureAnnualPremium= BigDecimal.ZERO;

    private BigDecimal basicPremium = BigDecimal.ZERO;

    private YesNo installmentAllowed;

    private BigDecimal installmentAmount; //payable premium instalment after first installment

    private BigDecimal installmentPremium; //next payable premium installment

    private String firstPremium;

    private String nextPremium;

    private List<QuoteRisk> quotationRisks = new ArrayList<>();

    private List<QuotePrdctTax> quotationProductTaxes = new ArrayList<>();

    //For Reports

    private String premiumAmountString;

    private String futureAnnualPremiumString;

    private String productDesc;

    private String valuerNotes;

}
