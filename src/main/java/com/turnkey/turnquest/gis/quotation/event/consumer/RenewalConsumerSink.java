package com.turnkey.turnquest.gis.quotation.event.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.turnkey.turnquest.gis.quotation.client.crm.AgentPanelClient;
import com.turnkey.turnquest.gis.quotation.client.crm.PanelClient;
import com.turnkey.turnquest.gis.quotation.dto.crm.AgentPanelDto;
import com.turnkey.turnquest.gis.quotation.event.producer.OnSyncedRenewalSave;
import com.turnkey.turnquest.gis.quotation.model.Quotation;
import com.turnkey.turnquest.gis.quotation.service.QuotationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Slf4j
@Component
public class RenewalConsumerSink {

    private final QuotationService quotationService;

    private final OnSyncedRenewalSave onSyncedRenewalSave;

    private final AgentPanelClient agentPanelClient;

    private final PanelClient panelClient;

    public RenewalConsumerSink(QuotationService quotationService, OnSyncedRenewalSave onSyncedRenewalSave,
                               AgentPanelClient agentPanelClient, PanelClient panelClient) {
        this.quotationService = quotationService;
        this.onSyncedRenewalSave = onSyncedRenewalSave;
        this.agentPanelClient = agentPanelClient;
        this.panelClient = panelClient;
    }

    @KafkaListener(topics = "process-renewals")
    public void renewalConsumer(@Payload String payload) throws JsonProcessingException {
        Quotation quotation = stringToPayload(payload);
        log.info("Renewal onboarding: received renewal {} ", new ObjectMapper().writeValueAsString(quotation));
        Quotation existingQuote = quotationService.findByRenewalBatchNo(quotation.getRenewalBatchNo()).orElse(null);
        if (existingQuote != null) {
            log.info("Renewal onboarding: existing renewal {} ", new ObjectMapper().writeValueAsString(existingQuote));
            quotation.setId(existingQuote.getId());
            quotation.setReadStatus(true);
            quotation.setPanelId(getPanelId(quotation.getOrganizationId(), quotation.getInsurerOrgId()));
        }
        quotation.setPanelId(getPanelId(quotation.getOrganizationId(), quotation.getInsurerOrgId()));
        quotation.setCurrencyId(35L);
        try {
            log.info("Saved Renewal Quote {}",new ObjectMapper().writeValueAsString(quotation));
            Quotation quote = quotationService.saveQuickQuotation(quotation, quotation.getOrganizationId());
            onSyncedRenewalSave.publishRenewal(quote.getRenewalBatchNo());
            log.info("Renewal onboarding: publishing saved renewal {} ", quotation.getPolicyNo());

        } catch (Exception e) {
            log.info("Renewal onboarding: An error occurred while saving a synced renewal {}", e.getMessage());
            e.printStackTrace();
        }
    }

    private Long getPanelId(Long agentOrganizationId, Long insurerOrganizationId) {
        var agentPanels = agentPanelClient.getByAgentOrgAndInsurerOrg(agentOrganizationId, insurerOrganizationId);
        var panelId = panelClient.getAllInsurerPanel(agentPanels.stream()
                .mapToLong(AgentPanelDto::getPanelId).boxed().collect(Collectors.toList()), "ALL");
        if (panelId != null) {
            return panelId.getId();
        }
        return -1L;
    }

    private Quotation stringToPayload(String payload) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper.readValue(payload, Quotation.class);
    }

}
