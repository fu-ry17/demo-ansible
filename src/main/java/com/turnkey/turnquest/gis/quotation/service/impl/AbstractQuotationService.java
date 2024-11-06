package com.turnkey.turnquest.gis.quotation.service.impl;

import com.turnkey.turnquest.gis.quotation.client.crm.AgencyClient;
import com.turnkey.turnquest.gis.quotation.client.crm.ClientDataClient;
import com.turnkey.turnquest.gis.quotation.client.crm.OrganizationClient;
import com.turnkey.turnquest.gis.quotation.dto.crm.AgencyDto;
import com.turnkey.turnquest.gis.quotation.dto.crm.ClientDto;
import com.turnkey.turnquest.gis.quotation.dto.crm.OrganizationDto;
import com.turnkey.turnquest.gis.quotation.enums.ValuationStatus;
import com.turnkey.turnquest.gis.quotation.model.*;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public abstract class AbstractQuotationService {

     final ClientDataClient clientDataClient;

     final AgencyClient agencyClient;


     final OrganizationClient organizationClient;
    static Logger log = LoggerFactory.getLogger(AbstractQuotationService.class);

    protected AbstractQuotationService(ClientDataClient clientDataClient, AgencyClient agencyClient, OrganizationClient organizationClient) {
        this.clientDataClient = clientDataClient;
        this.agencyClient = agencyClient;
        this.organizationClient = organizationClient;
    }

    @SneakyThrows
    @NotNull
    public List<QuotationRisk> checkAndRemoveDuplicateRisks(List<QuotationRisk> quotationRisks) {
        HashMap<String, QuotationRisk> uniqueRisks = new HashMap<>();
        for (var risk : quotationRisks) {
                if (uniqueRisks.containsKey(risk.getRiskId())) {
                    var existingRisk = uniqueRisks.get(risk.getRiskId());
                    if (existingRisk.getId() != null) continue;
                    if (risk.getId() != null) {
                        uniqueRisks.put(risk.getRiskId(), risk);
                    }
                } else {
                    uniqueRisks.put(risk.getRiskId(), risk);
                }
            }

        return new ArrayList<>(uniqueRisks.values());
    }

    public Quotation composeQuotation(Quotation quotation) {

        if (quotation.getClientId() != null) {
            ClientDto client = clientDataClient.findById(quotation.getClientId());
            quotation.setClient(client);
        }

        if (quotation.getPanelId() != null) {
            try {
                AgencyDto agency = agencyClient.findByPanelIdAndOrganizationId(quotation.getPanelId(), quotation.getOrganizationId());
                OrganizationDto org = organizationClient.findById(agency.getInsurerId());
                quotation.setOrganization(org);
                quotation.setAgent(agency);
            } catch (Exception ex) {
                log.error(ex.getMessage());
            }
        }

        return quotation;
    }

}
