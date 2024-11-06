package com.turnkey.turnquest.gis.quotation.controller.unit;

import com.turnkey.turnquest.gis.quotation.auth.TokenUtils;
import com.turnkey.turnquest.gis.quotation.controller.RenewalController;
import com.turnkey.turnquest.gis.quotation.dto.notification.PushNotificationDto;
import com.turnkey.turnquest.gis.quotation.enums.SortType;
import com.turnkey.turnquest.gis.quotation.event.producer.NotificationProducer;
import com.turnkey.turnquest.gis.quotation.model.Quotation;
import com.turnkey.turnquest.gis.quotation.service.QuotationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class RenewalControllerTest {

    @Mock
    QuotationService quotationService;

    @Mock
    TokenUtils tokenUtils;

    @Mock
    NotificationProducer notificationProducer;

    @InjectMocks
    RenewalController renewalController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateRenewal() throws Exception {
        var quote = createQuotation();
        var notificationDto = createPushNotificationDto();
        when(quotationService.saveQuickQuotation(any(Quotation.class), anyLong())).thenReturn(quote);
        doNothing().when(notificationProducer).queuePushNotification(notificationDto);

        var result = renewalController.createRenewal(quote);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(quote, result.getBody());
    }

    @Test
    void getRenewals(){
       Pageable pageable = PageRequest.of(0, 50, Sort.by("createdDate").descending());
        Authentication authentication = mock(Authentication.class);
        List<Quotation> expectedQuotations = Arrays.asList(createQuotation(), createQuotation());

        when(tokenUtils.getOrganizationId()).thenReturn(1L);
        when(quotationService.getRenewals(anyLong(), any(Pageable.class))).thenReturn(expectedQuotations);

        // Act
        ResponseEntity<List<Quotation>> result = renewalController.getRenewals(pageable, authentication);

        // Assert
        verify(tokenUtils).init(authentication);
        verify(quotationService).getRenewals(1L, pageable);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(expectedQuotations, result.getBody());
    }

    @Test
    void shouldReturnOkStatusWhenUnreadRenewalsFound() {
        // Arrange
        Authentication authentication = mock(Authentication.class);
        Mono<Integer> expectedUnreadRenewals = Mono.just(5);

        when(tokenUtils.getOrganizationId()).thenReturn(1L);
        when(quotationService.findUnreadRenewals(anyLong())).thenReturn(expectedUnreadRenewals);

        // Act
        ResponseEntity<Mono<Integer>> result = renewalController.findUnreadRenewals(authentication);

        // Assert
        verify(tokenUtils).init(authentication);
        verify(quotationService).findUnreadRenewals(1L);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(expectedUnreadRenewals, result.getBody());
    }

    @Test
    void shouldReturnOkStatusWhenNoUnreadRenewalsFound() {
        // Arrange
        Authentication authentication = mock(Authentication.class);
        Mono<Integer> expectedUnreadRenewals = Mono.just(0);

        when(tokenUtils.getOrganizationId()).thenReturn(1L);
        when(quotationService.findUnreadRenewals(anyLong())).thenReturn(expectedUnreadRenewals);

        // Act
        ResponseEntity<Mono<Integer>> result = renewalController.findUnreadRenewals(authentication);

        // Assert
        verify(tokenUtils).init(authentication);
        verify(quotationService).findUnreadRenewals(1L);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(expectedUnreadRenewals, result.getBody());
    }

    @Test
    void shouldReturnOkStatusWhenRenewalsFromDateFound() {
        // Arrange
        Authentication authentication = mock(Authentication.class);
        Integer expectedRenewals = 5;

        when(tokenUtils.getOrganizationId()).thenReturn(1L);
        when(quotationService.getNoRenewalsFromDate(anyLong(), anyLong(), anyLong())).thenReturn(expectedRenewals);

        // Act
        ResponseEntity<Integer> result = renewalController.getNoRenewalsFromDate(1L, 2L, authentication);

        // Assert
        verify(tokenUtils).init(authentication);
        verify(quotationService).getNoRenewalsFromDate(1L, 2L, 1L);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(expectedRenewals, result.getBody());
    }

    @Test
    void shouldReturnOkStatusWhenNoRenewalsFromDateFound() {
        // Arrange
        Authentication authentication = mock(Authentication.class);
        Integer expectedRenewals = 0;

        when(tokenUtils.getOrganizationId()).thenReturn(1L);
        when(quotationService.getNoRenewalsFromDate(anyLong(), anyLong(), anyLong())).thenReturn(expectedRenewals);

        // Act
        ResponseEntity<Integer> result = renewalController.getNoRenewalsFromDate(1L, 2L, authentication);

        // Assert
        verify(tokenUtils).init(authentication);
        verify(quotationService).getNoRenewalsFromDate(1L, 2L, 1L);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(expectedRenewals, result.getBody());
    }


    @Test
    void shouldReturnQuotationsWhenSortAndFilterParametersAreProvided() {
        String searchText = "test";
        Long clientId = 1L;
        Long insOrgId = 1L;
        Long coverFromDate = 1L;
        Long coverToDate = 1L;
        BigDecimal priceFrom = BigDecimal.valueOf(1000);
        BigDecimal priceTo = BigDecimal.valueOf(2000);
        SortType sortBy = SortType.ASC;
        SortType sortPrice = SortType.DESC;
        int page = 0;
        int size = 50;
        Authentication authentication = mock(Authentication.class);
        List<Quotation> expectedQuotations = Arrays.asList(new Quotation(), new Quotation());

        when(tokenUtils.getOrganizationId()).thenReturn(1L);
        when(quotationService.sortFilterAndSearch(searchText, clientId, insOrgId, coverFromDate, coverToDate, priceFrom,
                priceTo, sortBy, sortPrice, page, size, tokenUtils.getOrganizationId(), "RN")).thenReturn(expectedQuotations);

        ResponseEntity<List<Quotation>> result = renewalController.sortAndFilter(searchText, clientId, insOrgId, coverFromDate, coverToDate, priceFrom,
                priceTo, sortBy, sortPrice, page, size, authentication);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(expectedQuotations, result.getBody());
    }

    @Test
    void shouldReturnEmptyListWhenNoQuotationsMatchSortAndFilterParameters() {
        String searchText = "test";
        Long clientId = 1L;
        Long insOrgId = 1L;
        Long coverFromDate = 1L;
        Long coverToDate = 1L;
        BigDecimal priceFrom = BigDecimal.valueOf(1000);
        BigDecimal priceTo = BigDecimal.valueOf(2000);
        SortType sortBy = SortType.ASC;
        SortType sortPrice = SortType.DESC;
        int page = 0;
        int size = 50;
        Authentication authentication = mock(Authentication.class);

        when(tokenUtils.getOrganizationId()).thenReturn(1L);
        when(quotationService.sortFilterAndSearch(searchText, clientId, insOrgId, coverFromDate, coverToDate, priceFrom,
                priceTo, sortBy, sortPrice, page, size, tokenUtils.getOrganizationId(), "RN")).thenReturn(Collections.emptyList());

        ResponseEntity<List<Quotation>> result = renewalController.sortAndFilter(searchText, clientId, insOrgId, coverFromDate, coverToDate, priceFrom,
                priceTo, sortBy, sortPrice, page, size, authentication);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertTrue(result.getBody().isEmpty());
    }




    private Quotation createQuotation() {
        Quotation quotation = new Quotation();
        quotation.setOrganizationId(1L);
        quotation.setId(2L);
        quotation.setPolicyNo("testPolicyNo");
        return quotation;
    }

    private PushNotificationDto createPushNotificationDto() {
        PushNotificationDto pushNotificationDto = new PushNotificationDto();
        pushNotificationDto.setTemplateCode("RN_CONVERSION_COMPLETE");
        pushNotificationDto.setId("1");
        pushNotificationDto.setOrganizationId(1L);
        pushNotificationDto.setAttributes(Collections.singletonMap("[POLICY_NO]", "testPolicyNo"));

        return pushNotificationDto;
    }
}
