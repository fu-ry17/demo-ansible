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
import com.turnkey.turnquest.gis.quotation.service.DealService;
import com.turnkey.turnquest.gis.quotation.service.QuotationService;
import com.turnkey.turnquest.gis.quotation.specifications.QuotationsSpecifications;
import com.turnkey.turnquest.gis.quotation.util.EntitySpecification;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service("dealService")
public class DealServiceImpl implements DealService {

    private final DealRepository dealRepository;
    private final ClientDataClient clientClient;
    private final OrganizationClient organizationClient;
    private final CloseTodoEvent closeTodoEvent;
    private final QuotationService quotationService;
    private final CreateDealEvent createDealEvent;

    public DealServiceImpl(DealRepository dealRepository, ClientDataClient clientClient, OrganizationClient organizationClient,
                           CloseTodoEvent closeTodoEvent, QuotationService quotationService, CreateDealEvent createDealEvent) {
        this.dealRepository = dealRepository;
        this.clientClient = clientClient;
        this.organizationClient = organizationClient;
        this.closeTodoEvent = closeTodoEvent;
        this.quotationService = quotationService;
        this.createDealEvent = createDealEvent;
    }

    /**
     * Finds a Deal based on the ID and OrganizationID
     *
     * @param id
     * @param organizationId
     * @return
     */
    @Override
    public Deal findById(Long id, Long organizationId) {
        return composeDeal(dealRepository.findByIdAndOrganizationId(id, organizationId));
    }

    /**
     * Finds a list of deals based on the Organisation ID
     *
     * @param organizationId
     * @return List
     */
    @Override
    public List<Deal> findAll(Long organizationId, Pageable pageable) {
        return dealRepository.findAllByOrganizationId(organizationId, pageable)
                .stream().map(this::composeDeal).collect(Collectors.toList());
    }

    /**
     * Finds all deals based on quotationId
     *
     * @param quoteId
     * @param organizationId
     * @return
     */
    @Override
    public List<Deal> filterDealsByQuotationId(Long quoteId, Long organizationId) {
        return dealRepository.findAllByQuotationIdAndOrganizationId(quoteId, organizationId);
    }

    /**
     * Saves a single Deal item
     *
     * @param deal
     * @return Deal
     */
    @Override
    public Deal save(Deal deal, Long organizationId) {
        if (deal.getOrganization() == null || deal.getAssignedTo() == null) {
            deal.setOrganizationId(organizationId);
            deal.setAssignedTo(organizationId);
        }
        Deal finalDeal = dealRepository.save(deal);
        createDealEvent.dealCreated(finalDeal);
        return finalDeal;
    }

    /**
     * Closes a deal after closing all related 'todos'
     *
     * @param deal
     * @param organizationId
     * @return
     */
    @Override
    public Deal closeDeal(Deal deal, Long organizationId) {
        if (deal.getId() != null) {
            closeTodoEvent.closeTodo(deal.getId());
            deal.setOverAllStatus(OverAllStatus.CLOSED);
            Deal finalDeal = dealRepository.save(deal);
            createDealEvent.dealCreated(finalDeal);
            return finalDeal;
        } else {
            return new Deal();
        }
    }

    @Override
    public List<Deal> closeDeals(List<Deal> deals) {
        List<Deal> agentDeals = deals.stream()
                .map(deal -> {
                    closeTodoEvent.closeTodo(deal.getId());
                    deal.setOverAllStatus(OverAllStatus.CLOSED);
                    return deal;
                })
                .collect(Collectors.toList());

        return dealRepository.saveAll(agentDeals);
    }

    /**
     * Saves a list of existing deals
     *
     * @param deals
     * @param organizationId
     * @return List.
     */
    @Override
    public List<Deal> saveAll(List<Deal> deals, Long organizationId) {
        deals = deals.stream().map(deal -> {
            deal.setOrganizationId(organizationId);
            deal.setAssignedTo(organizationId);
            return deal;
        }).collect(Collectors.toList());

        return dealRepository.saveAll(deals)
                .stream().map(deal -> {
                    createDealEvent.dealCreated(deal);
                    return deal;
                })
                .collect(Collectors.toList());

    }

    /**
     * Deletes an existing Deal
     *
     * @param deal
     */
    @Override
    public void delete(Deal deal) {
        dealRepository.delete(deal);
    }

    /**
     * Filters the deals according to the DealStatus
     *
     * @param dealStatus
     * @param organizationId
     * @return List.
     */
    @Override
    public List<Deal> filterByDealsStatus(DealStatus dealStatus, Long organizationId, Pageable pageable) {
        Page<Deal> deals = dealRepository.findAllByOrganizationId(organizationId, pageable);
        List<Deal> filteredDeals = deals.stream()
                .filter(deal -> deal.getDealStatus() == dealStatus)
                .collect(Collectors.toList());

        return filteredDeals.stream().map(this::composeDeal).collect(Collectors.toList());

    }

    /**
     * Filters the deals according to the overall status
     *
     * @param overAllStatus
     * @param organizationId
     * @return
     */
    @Override
    public List<Deal> filterByOverAllStatus(OverAllStatus overAllStatus, Long organizationId, Pageable pageable) {
        Page<Deal> deals = dealRepository.findAllByOrganizationId(organizationId, pageable);
        return deals.stream()
                .filter(deal -> deal.getOverAllStatus() == overAllStatus)
                .map(this::composeDeal)
                .collect(Collectors.toList());
    }

    /**
     * Filter Deals based on client IDs
     *
     * @param clientIds
     * @param organizationId
     * @return
     */
    @Override
    public List<Deal> filterDealsByClients(List<Long> clientIds, Long organizationId, Pageable pageable) {

        return dealRepository.findAllByClientIdInAndOrganizationId(clientIds, organizationId, pageable)
                .stream()
                .map(this::composeDeal)
                .collect(Collectors.toList());

    }

    @Override
    public List<Deal> sortFilterAndSearch(String searchText, Long clientId, Long insOrgId, Long coverFromDate,
                                          Long coverToDate, BigDecimal priceFrom, BigDecimal priceTo, SortType sortBy,
                                          SortType sortPrice, int page, int size, Long organizationId) {

        Pageable pageable = PageRequest.of(page, size);

        if (sortBy != null && sortPrice != null) {
            pageable = PageRequest.of(page, size, Sort.by(new Sort.Order(Sort.Direction.fromString(sortPrice.name()), "amount"),
                    new Sort.Order(Sort.Direction.fromString(sortBy.name()), "createdDate")));
        } else if (sortBy == null && sortPrice != null) {
            pageable = PageRequest.of(page, size, Sort.by(new Sort.Order(Sort.Direction.fromString(sortPrice.name()), "amount")));
        } else if (sortBy != null) {
            pageable = PageRequest.of(page, size, Sort.by(new Sort.Order(Sort.Direction.fromString(sortBy.name()), "createdDate")));
        }


        Page<Deal> deals;
        QuotationsSpecifications<Deal> quotationsSpecifications = new QuotationsSpecifications<>();
        if (searchText != null) {
            EntitySpecification<Deal> entitySpecification = new EntitySpecification<>();
            //deals = dealRepository.findAll(Specification.where(EntitySpecification.textInAllColumns(searchText)), pageable);
            deals = dealRepository.findAll(Specification.where(entitySpecification.textInAllColumns(searchText))
                    .and(quotationsSpecifications.longValueEquals("organizationId", organizationId))
                    .and(quotationsSpecifications.longValueEquals("clientId", clientId))
                    .and(quotationsSpecifications.longValueBetween("createdDate",coverFromDate, coverToDate))
                    .and(quotationsSpecifications.bigDecimalValueBetween("amount",priceFrom, priceTo)), pageable);
        } else {
            //deals = dealRepository.findAllByOrganizationId(organizationId, pageable);
            deals = dealRepository.findAll(Specification.where(quotationsSpecifications.longValueEquals("organizationId", organizationId))
                    .and(quotationsSpecifications.longValueEquals("clientId", clientId))
                    .and(quotationsSpecifications.longValueBetween("createdDate",coverFromDate, coverToDate))
                    .and(quotationsSpecifications.bigDecimalValueBetween("amount",priceFrom, priceTo)), pageable);
        }
        //return filterQuotations(clientId, insOrgId, coverFromDate, coverToDate, priceFrom, priceTo, organizationId, deals);
        return  deals.stream().map(this::composeDeal)
                .collect(Collectors.toList());
    }

    @NotNull
    private List<Deal> filterQuotations(Long clientId, Long insOrgId, Long coverFromDate, Long coverToDate, BigDecimal priceFrom, BigDecimal priceTo, Long organizationId, Page<Deal> dealPage) {
        List<Deal> deals = dealPage
                .stream()
                .filter(deal -> deal.getOrganizationId().equals(organizationId))
                .collect(Collectors.toList());
        if (clientId != null) {
            deals = deals.stream()
                    .filter(Objects::nonNull)
                    .filter(deal -> deal.getClientId() != null)
                    .filter(deal -> deal.getClientId().equals(clientId))
                    .collect(Collectors.toList());
        }

        if (coverFromDate != null && coverToDate != null) {
            deals = deals.stream()
                    .filter(Objects::nonNull)
                    .filter(deal -> deal.getCreatedDate() != null)
                    .filter(deal -> (deal.getCreatedDate() >= coverFromDate && deal.getCreatedDate() <= coverToDate))
                    .collect(Collectors.toList());
        }

        if (priceFrom != null && priceTo != null) {
            deals = deals.stream()
                    .filter(Objects::nonNull)
                    .filter(deal -> deal.getAmount() != null)
                    .filter(deal -> (deal.getAmount().compareTo(priceFrom) > 0 && deal.getAmount().compareTo(priceTo) < 0))
                    .collect(Collectors.toList());
        }

        if (insOrgId != null) {
            return deals.stream()
                    .filter(Objects::nonNull)
                    .map(this::composeDeal)
                    .filter(deal -> deal.getOrganization().getId().equals(insOrgId))
                    .collect(Collectors.toList());
        } else {
            return deals.stream()
                    .filter(Objects::nonNull)
                    .map(this::composeDeal)
                    .collect(Collectors.toList());
        }
    }


    /**
     * Sets the respective Client ID, Org ID and Quote ID
     *
     * @param deal
     * @return
     */
    Deal composeDeal(Deal deal) {
        if (deal.getClientId() != null) {
            ClientDto clientDto = clientClient.findById(deal.getClientId());
            deal.setClient(clientDto);
        }
        if (deal.getInsurerOrgId() != null) {
            OrganizationDto organization = organizationClient.findById(deal.getInsurerOrgId());
            deal.setOrganization(organization);
        }
        if (deal.getQuotationId() != null) {
            quotationService.findById(deal.getQuotationId(), deal.getOrganizationId())
                            .ifPresent(deal::setQuotation);
        }

        return deal;
    }


}
