package com.turnkey.turnquest.gis.quotation.controller.unit;

import com.turnkey.turnquest.gis.quotation.controller.DealController;
import com.turnkey.turnquest.gis.quotation.service.DealService;
import com.turnkey.turnquest.gis.quotation.auth.TokenUtils;
import com.turnkey.turnquest.gis.quotation.model.Deal;
import com.turnkey.turnquest.gis.quotation.enums.DealStatus;
import com.turnkey.turnquest.gis.quotation.enums.OverAllStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class DealControllerTest {

    @Mock
    private DealService dealService;

    @Mock
    private TokenUtils tokenUtils;

    @InjectMocks
    private DealController dealController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldGetAllDeals() {
        Pageable pageable = PageRequest.of(0, 50);
        when(dealService.findAll(any(), any())).thenReturn(Collections.emptyList());

        ResponseEntity<List<Deal>> response = dealController.getAllDeals(pageable, null);

        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void shouldGetOneById() {
        Deal deal = new Deal();
        when(dealService.findById(any(), any())).thenReturn(deal);

        ResponseEntity<Deal> response = dealController.getOneById(1L, null);

        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void shouldFilterByDealStatus() {
        Pageable pageable = PageRequest.of(0, 50);
        when(dealService.filterByDealsStatus(any(), any(), any())).thenReturn(Collections.emptyList());

        ResponseEntity<List<Deal>> response = dealController.filterByDealStatus(pageable, DealStatus.PENDING, null);

        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void shouldFilterByOverAllStatus() {
        Pageable pageable = PageRequest.of(0, 50);
        when(dealService.filterByOverAllStatus(any(), any(), any())).thenReturn(Collections.emptyList());

        ResponseEntity<List<Deal>> response = dealController.filterByOverAllStatus(pageable, OverAllStatus.OPEN, null);

        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void shouldFilterByClientId() {
        Pageable pageable = PageRequest.of(0, 50);
        when(dealService.filterDealsByClients(any(), any(), any())).thenReturn(Collections.emptyList());

        ResponseEntity<List<Deal>> response = dealController.filterByClientId(pageable, Collections.emptyList(), null);

        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void shouldFilterByDealId() {
        when(dealService.filterDealsByQuotationId(any(), any())).thenReturn(Collections.emptyList());

        ResponseEntity<List<Deal>> response = dealController.filterByDealId(1L, 1L);

        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void shouldCloseDealsByQuote() {
        when(dealService.closeDeals(any())).thenReturn(Collections.emptyList());

        ResponseEntity<List<Deal>> response = dealController.closeDealsByQuote(Collections.emptyList());

        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void shouldCloseDeal() {
        Deal deal = new Deal();
        when(dealService.closeDeal(any(), any())).thenReturn(deal);

        ResponseEntity<Deal> response = dealController.closeDeal(deal, null);

        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void shouldSaveDeal() {
        Deal deal = new Deal();
        when(dealService.save(any(), any())).thenReturn(deal);

        ResponseEntity<Deal> response = dealController.saveDeal(deal, null);

        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void shouldSaveAllDeals() {
        when(dealService.saveAll(any(), any())).thenReturn(Collections.emptyList());

        ResponseEntity<List<Deal>> response = dealController.saveAllDeals(Collections.emptyList(), null);

        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void shouldDeleteDeal() {
        Deal deal = new Deal();

        ResponseEntity<Deal> response = dealController.deleteDeal(deal);

        assertEquals(204, response.getStatusCodeValue());
    }

}