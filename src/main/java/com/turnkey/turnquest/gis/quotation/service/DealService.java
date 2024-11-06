package com.turnkey.turnquest.gis.quotation.service;

import com.turnkey.turnquest.gis.quotation.enums.DealStatus;
import com.turnkey.turnquest.gis.quotation.enums.OverAllStatus;
import com.turnkey.turnquest.gis.quotation.enums.SortType;
import com.turnkey.turnquest.gis.quotation.model.Deal;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface DealService {

    /**
     * @param id
     * @param organizationId
     * @return
     */
    Deal findById(Long id, Long organizationId);

    /**
     * @param organizationId
     * @param pageable
     * @return
     */
    List<Deal> findAll(Long organizationId, Pageable pageable);

    /**
     * @param quoteId
     * @param organizationId
     * @return
     */
    List<Deal> filterDealsByQuotationId(Long quoteId, Long organizationId);

    /**
     * @param deal
     * @param organizationId
     * @return
     */
    Deal save(Deal deal, Long organizationId);

    /**
     * @param deals
     * @return
     */
    List<Deal> closeDeals(List<Deal> deals);

    /**
     * @param deal
     * @param organizationId
     * @return
     */
    Deal closeDeal(Deal deal, Long organizationId);

    /**
     * @param deals
     * @param organizationId
     * @return
     */
    List<Deal> saveAll(List<Deal> deals, Long organizationId);

    /**
     * @param deal
     */
    void delete(Deal deal);

    /**
     * @param dealStatus
     * @param organizationId
     * @param pageable
     * @return
     */
    List<Deal> filterByDealsStatus(DealStatus dealStatus, Long organizationId, Pageable pageable);

    /**
     * @param overAllStatus
     * @param organizationId
     * @param pageable
     * @return
     */
    List<Deal> filterByOverAllStatus(OverAllStatus overAllStatus, Long organizationId, Pageable pageable);

    /**
     * @param clientIds
     * @param organizationId
     * @param pageable
     * @return
     */
    List<Deal> filterDealsByClients(List<Long> clientIds, Long organizationId, Pageable pageable);

    /**
     * @param searchText
     * @param clientId
     * @param insOrgId
     * @param coverFromDate
     * @param coverToDate
     * @param priceFrom
     * @param priceTo
     * @param sortBy
     * @param sortPrice
     * @param page
     * @param size
     * @return
     */
    List<Deal> sortFilterAndSearch(String searchText, Long clientId, Long insOrgId, Long coverFromDate,
                                   Long coverToDate, BigDecimal priceFrom, BigDecimal priceTo, SortType sortBy,
                                   SortType sortPrice, int page, int size, Long organizationId);
}
