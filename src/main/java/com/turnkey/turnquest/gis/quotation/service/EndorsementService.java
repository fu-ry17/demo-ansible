package com.turnkey.turnquest.gis.quotation.service;

import com.turnkey.turnquest.gis.quotation.dto.endorsement.PolicyActiveRiskDto;
import com.turnkey.turnquest.gis.quotation.enums.YesNo;
import com.turnkey.turnquest.gis.quotation.model.Quotation;

import java.util.List;

public interface EndorsementService {

    List<PolicyActiveRiskDto> saveActiveRisks(Quotation quotation, YesNo isAnnual);

    Quotation computeFirstInstallment(Quotation quotation);

    Quotation getQuotationToPay(String quotationNumber);
    Quotation saveAnnualQuotationRisk(Quotation quotation, YesNo isAnnual);

}
