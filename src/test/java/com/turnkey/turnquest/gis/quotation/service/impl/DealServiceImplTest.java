package com.turnkey.turnquest.gis.quotation.service.impl;

import com.turnkey.turnquest.gis.quotation.client.crm.ClientDataClient;
import com.turnkey.turnquest.gis.quotation.client.crm.OrganizationClient;
import com.turnkey.turnquest.gis.quotation.dto.crm.ClientDto;
import com.turnkey.turnquest.gis.quotation.dto.crm.OrganizationDto;
import com.turnkey.turnquest.gis.quotation.enums.DealStatus;
import com.turnkey.turnquest.gis.quotation.enums.OverAllStatus;
import com.turnkey.turnquest.gis.quotation.enums.SortType;
import com.turnkey.turnquest.gis.quotation.event.producer.CloseTodoEvent;
import com.turnkey.turnquest.gis.quotation.event.producer.CreateDealEvent;
import com.turnkey.turnquest.gis.quotation.model.Deal;
import com.turnkey.turnquest.gis.quotation.model.Quotation;
import com.turnkey.turnquest.gis.quotation.repository.DealRepository;
import com.turnkey.turnquest.gis.quotation.service.QuotationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DealServiceImplTest {

    @Mock
    private DealRepository dealRepository;
    @Mock
    private ClientDataClient clientClient;
    @Mock
    private OrganizationClient organizationClient;
    @Mock
    private CloseTodoEvent closeTodoEvent;
    @Mock
    private QuotationService quotationService;
    @Mock
    private CreateDealEvent createDealEvent;

    private DealServiceImpl dealService;

    @BeforeEach
    void setUp() {
        dealService = new DealServiceImpl(dealRepository, clientClient, organizationClient, closeTodoEvent, quotationService, createDealEvent);
    }

    @Test
    void shouldFindDealByIdAndOrganizationId() {
        when(dealRepository.findByIdAndOrganizationId(any(), any())).thenReturn(new Deal());
        dealService.findById(1L, 1L);
        verify(dealRepository).findByIdAndOrganizationId(any(), any());
    }

    @Test
    void shouldFilterDealsByQuotationIdWhenDealsExist() {
        // Given
        Long quoteId = 1L;
        Long organizationId = 1L;
        Deal deal1 = new Deal();
        Deal deal2 = new Deal();
        List<Deal> deals = Arrays.asList(deal1, deal2);
        when(dealRepository.findAllByQuotationIdAndOrganizationId(quoteId, organizationId)).thenReturn(deals);

        // When
        List<Deal> result = dealService.filterDealsByQuotationId(quoteId, organizationId);

        // Then
        assertEquals(2, result.size());
        verify(dealRepository, times(1)).findAllByQuotationIdAndOrganizationId(quoteId, organizationId);
    }

    @Test
    void shouldFindAllDealsByOrganizationId() {
        when(dealRepository.findAllByOrganizationId(any(), any())).thenReturn(Page.empty());
        dealService.findAll(1L, null);
        verify(dealRepository).findAllByOrganizationId(any(), any());
    }

    @Test
    void shouldSaveDeal() {
        when(dealRepository.save(any())).thenReturn(new Deal());
        dealService.save(new Deal(), 1L);
        verify(dealRepository).save(any());
    }

    @Test
    void shouldCloseDeal() {
        Deal deal = new Deal();
        deal.setId(1L);
        when(dealRepository.save(any())).thenReturn(deal);
        dealService.closeDeal(deal, 1L);
        verify(closeTodoEvent).closeTodo(any());
        verify(dealRepository).save(any());
    }

    @Test
    void shouldCloseDealsWhenDealsArePresent() {
        // Given
        List<Deal> deals = Arrays.asList(new Deal(), new Deal());
        when(dealRepository.saveAll(any())).thenReturn(deals);

        // When
        List<Deal> closedDeals = dealService.closeDeals(deals);

        // Then
        assertEquals(deals.size(), closedDeals.size());
        verify(closeTodoEvent, times(deals.size())).closeTodo(any());
        verify(dealRepository).saveAll(any());
    }

    @Test
    void shouldSaveAllDealsWithOrganizationIdAssigned() {
        // Given
        List<Deal> deals = Arrays.asList(new Deal(), new Deal());
        when(dealRepository.saveAll(any())).thenReturn(deals);

        // When
        List<Deal> savedDeals = dealService.saveAll(deals, 1L);

        // Then
        assertEquals(deals.size(), savedDeals.size());
        savedDeals.forEach(deal -> {
            assertEquals(1L, deal.getOrganizationId());
            assertEquals(1L, deal.getAssignedTo());
        });
        verify(createDealEvent, times(deals.size())).dealCreated(any());
    }

    @Test
    void shouldDeleteDealWhenDealExists() {
        // Given
        Deal deal = new Deal();
        deal.setId(1L);

        // When
        dealService.delete(deal);

        // Then
        verify(dealRepository).delete(any());
    }

    @Test
    void shouldFilterDealsByStatus() {
        // Given
        Page<Deal> deals = Page.empty();
        when(dealRepository.findAllByOrganizationId(any(), any())).thenReturn(deals);

        // When
        List<Deal> filteredDeals = dealService.filterByDealsStatus(DealStatus.PENDING, 1L, Pageable.unpaged());

        // Then
        assertTrue(filteredDeals.isEmpty());
        verify(dealRepository).findAllByOrganizationId(any(), any());
    }

    @Test
    void shouldFilterDealsByOverAllStatus() {
        // Given
        Page<Deal> deals = Page.empty();
        when(dealRepository.findAllByOrganizationId(any(), any())).thenReturn(deals);

        // When
        List<Deal> filteredDeals = dealService.filterByOverAllStatus(OverAllStatus.OPEN, 1L, Pageable.unpaged());

        // Then
        assertTrue(filteredDeals.isEmpty());
        verify(dealRepository).findAllByOrganizationId(any(), any());
    }

    @Test
    void shouldFilterDealsByClients() {
        // Given
        List<Long> clientIds = Arrays.asList(1L, 2L);
        when(dealRepository.findAllByClientIdInAndOrganizationId(any(), any(), any())).thenReturn(Page.empty());

        // When
        List<Deal> filteredDeals = dealService.filterDealsByClients(clientIds, 1L, Pageable.unpaged());

        // Then
        assertTrue(filteredDeals.isEmpty());
        verify(dealRepository).findAllByClientIdInAndOrganizationId(any(), any(), any());
    }

    @Test
    void shouldSortFilterAndSearchWithAllParameters() {
        // Given
        String searchText = "search";
        Long clientId = 1L;
        Long insOrgId = 1L;
        Long coverFromDate = 1L;
        Long coverToDate = 2L;
        BigDecimal priceFrom = BigDecimal.ONE;
        BigDecimal priceTo = BigDecimal.TEN;
        SortType sortBy = SortType.ASC;
        SortType sortPrice = SortType.DESC;
        int page = 0;
        int size = 10;
        Long organizationId = 1L;

        Page<Deal> deals = Page.empty();
        when(dealRepository.findAll((Specification<Deal>) any(), any())).thenReturn(deals);

        // When
        List<Deal> result = dealService.sortFilterAndSearch(searchText, clientId, insOrgId, coverFromDate, coverToDate, priceFrom, priceTo, sortBy, sortPrice, page, size, organizationId);

        // Then
        assertTrue(result.isEmpty());
        verify(dealRepository).findAll((Specification<Deal>) any(), any());
    }

    @Test
    void shouldSortFilterAndSearchWithNullParameters() {
        // Given
        String searchText = null;
        Long clientId = null;
        Long insOrgId = null;
        Long coverFromDate = null;
        Long coverToDate = null;
        BigDecimal priceFrom = null;
        BigDecimal priceTo = null;
        SortType sortBy = null;
        SortType sortPrice = null;
        int page = 0;
        int size = 10;
        Long organizationId = 1L;

        Page<Deal> deals = Page.empty();
        when(dealRepository.findAll((Specification<Deal>) any(), any())).thenReturn(deals);

        // When
        List<Deal> result = dealService.sortFilterAndSearch(searchText, clientId, insOrgId, coverFromDate, coverToDate, priceFrom, priceTo, sortBy, sortPrice, page, size, organizationId);

        // Then
        assertTrue(result.isEmpty());
        verify(dealRepository).findAll((Specification<Deal>) any(), any());
    }

    @Test
    void shouldComposeDealWithClient() {
        // Given
        Deal deal = new Deal();
        deal.setClientId(1L);
        ClientDto clientDto = new ClientDto();
        when(clientClient.findById(anyLong())).thenReturn(clientDto);

        // When
        Deal result = dealService.composeDeal(deal);

        // Then
        assertEquals(clientDto, result.getClient());
    }

    @Test
    void shouldComposeDealWithOrganization() {
        // Given
        Deal deal = new Deal();
        deal.setInsurerOrgId(1L);
        OrganizationDto organizationDto = new OrganizationDto();
        when(organizationClient.findById(anyLong())).thenReturn(organizationDto);

        // When
        Deal result = dealService.composeDeal(deal);

        // Then
        assertEquals(organizationDto, result.getOrganization());
    }

    @Test
    void shouldComposeDealWithQuotation() {
        // Given
        Deal deal = new Deal();
        deal.setQuotationId(1L);
        deal.setOrganizationId(1L);
        Quotation quotation = new Quotation();
        when(quotationService.findById(anyLong(), anyLong())).thenReturn(Optional.of(quotation));

        // When
        Deal result = dealService.composeDeal(deal);

        // Then
        assertEquals(quotation, result.getQuotation());
    }

    @Test
    void shouldComposeDealWithoutClient() {
        // Given
        Deal deal = new Deal();

        // When
        Deal result = dealService.composeDeal(deal);

        // Then
        assertNull(result.getClient());
    }

    @Test
    void shouldComposeDealWithoutOrganization() {
        // Given
        Deal deal = new Deal();

        // When
        Deal result = dealService.composeDeal(deal);

        // Then
        assertNull(result.getOrganization());
    }

    @Test
    void shouldComposeDealWithoutQuotation() {
        // Given
        Deal deal = new Deal();

        // When
        Deal result = dealService.composeDeal(deal);

        // Then
        assertNull(result.getQuotation());
    }

    @Test
    void shouldSortFilterAndSearchWithSortByNull() {
        // Given
        String searchText = "search";
        Long clientId = 1L;
        Long insOrgId = 1L;
        Long coverFromDate = 1L;
        Long coverToDate = 2L;
        BigDecimal priceFrom = BigDecimal.ONE;
        BigDecimal priceTo = BigDecimal.TEN;
        SortType sortBy = null;
        SortType sortPrice = SortType.DESC;
        int page = 0;
        int size = 10;
        Long organizationId = 1L;

        Page<Deal> deals = Page.empty();
        when(dealRepository.findAll((Specification<Deal>) any(), any())).thenReturn(deals);

        // When
        List<Deal> result = dealService.sortFilterAndSearch(searchText, clientId, insOrgId, coverFromDate, coverToDate, priceFrom, priceTo, sortBy, sortPrice, page, size, organizationId);

        // Then
        assertTrue(result.isEmpty());
        verify(dealRepository).findAll((Specification<Deal>) any(), any());
    }

    @Test
    void shouldSortFilterAndSearchWithSortPriceNull() {
        // Given
        String searchText = "search";
        Long clientId = 1L;
        Long insOrgId = 1L;
        Long coverFromDate = 1L;
        Long coverToDate = 2L;
        BigDecimal priceFrom = BigDecimal.ONE;
        BigDecimal priceTo = BigDecimal.TEN;
        SortType sortBy = SortType.ASC;
        SortType sortPrice = null;
        int page = 0;
        int size = 10;
        Long organizationId = 1L;

        Page<Deal> deals = Page.empty();
        when(dealRepository.findAll((Specification<Deal>) any(), any())).thenReturn(deals);

        // When
        List<Deal> result = dealService.sortFilterAndSearch(searchText, clientId, insOrgId, coverFromDate, coverToDate, priceFrom, priceTo, sortBy, sortPrice, page, size, organizationId);

        // Then
        assertTrue(result.isEmpty());
        verify(dealRepository).findAll((Specification<Deal>) any(), any());
    }

}
