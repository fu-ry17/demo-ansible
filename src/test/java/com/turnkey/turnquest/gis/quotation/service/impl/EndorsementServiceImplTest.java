package com.turnkey.turnquest.gis.quotation.service.impl;

import com.turnkey.turnquest.gis.quotation.client.endorsement.EndorsementClient;
import com.turnkey.turnquest.gis.quotation.client.crm.ClientDataClient;
import com.turnkey.turnquest.gis.quotation.client.crm.AgencyClient;
import com.turnkey.turnquest.gis.quotation.client.crm.OrganizationClient;
import com.turnkey.turnquest.gis.quotation.dto.crm.AgencyDto;
import com.turnkey.turnquest.gis.quotation.model.*;
import com.turnkey.turnquest.gis.quotation.enums.YesNo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class EndorsementServiceImplTest {

    @Mock
    private EndorsementClient endorsementClient;

    @Mock
    private ClientDataClient clientDataClient;

    @Mock
    private AgencyClient agencyClient;

    @Mock
    private OrganizationClient organizationClient;

    @InjectMocks
    private EndorsementServiceImpl endorsementService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldSaveActiveRisks() {
        QuotationRisk quotationRisk = new QuotationRisk();
        quotationRisk.setId(1L);
        quotationRisk.setQuotationRiskSections(List.of(new QuotationRiskSection()));
        quotationRisk.setQuoteDocument(List.of(new QuoteDocument()));
        QuotationProduct quotationProduct = new QuotationProduct();
        quotationProduct.setAgentCode("agentCode");
        quotationProduct.setQuotationRisks(List.of(quotationRisk));
        quotationProduct.setId(1L);
        Quotation quotation = new Quotation();
        quotation.setAgent(new AgencyDto());
        quotation.setQuotationProducts(List.of(quotationProduct));

        when(endorsementClient.saveAllActiveRisk(any())).thenReturn(null);

        endorsementService.saveActiveRisks(quotation, YesNo.Y);

        verify(endorsementClient).saveAllActiveRisk(any());
    }

    @Test
    void shouldSaveAnnualQuotationRisk() {
        QuotationRisk quotationRisk = new QuotationRisk();
        quotationRisk.setId(1L);
        quotationRisk.setQuotationRiskSections(List.of(new QuotationRiskSection()));
        quotationRisk.setQuoteDocument(List.of(new QuoteDocument()));
        QuotationProduct quotationProduct = new QuotationProduct();
        quotationProduct.setAgentCode("agentCode");
        quotationProduct.setQuotationRisks(List.of(quotationRisk));
        quotationProduct.setId(1L);
        Quotation quotation = new Quotation();
        quotation.setAgent(new AgencyDto());
        quotation.setQuotationProducts(List.of(quotationProduct));
        when(endorsementClient.saveAnnualActiveRisk(any())).thenReturn(null);

        endorsementService.saveAnnualQuotationRisk(quotation, YesNo.Y);

        verify(endorsementClient).saveAnnualActiveRisk(any());
    }

    @Test
    void shouldComputeFirstInstallment() {
        QuotationRisk quotationRisk = new QuotationRisk();
        quotationRisk.setId(1L);
        quotationRisk.setQuotationRiskSections(List.of(new QuotationRiskSection()));
        quotationRisk.setQuoteDocument(List.of(new QuoteDocument()));
        QuotationProduct quotationProduct = new QuotationProduct();
        quotationProduct.setAgentCode("agentCode");
        quotationProduct.setQuotationRisks(List.of(quotationRisk));
        quotationProduct.setId(1L);
        Quotation quotation = new Quotation();
        quotation.setAgent(new AgencyDto());
        quotation.setQuotationProducts(List.of(quotationProduct));
        when(endorsementClient.computeQuoteFirstInstallment(any())).thenReturn(quotation);

        endorsementService.computeFirstInstallment(quotation);

        verify(endorsementClient).computeQuoteFirstInstallment(any());
    }

    @Test
    void shouldGetQuotationToPay() {
        when(endorsementClient.findActiveRiskByQuotationNumber(any())).thenReturn(null);

        endorsementService.getQuotationToPay("quotationNumber");

        verify(endorsementClient).findActiveRiskByQuotationNumber(any());
    }

}
