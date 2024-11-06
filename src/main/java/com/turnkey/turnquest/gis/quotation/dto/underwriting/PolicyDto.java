package com.turnkey.turnquest.gis.quotation.dto.underwriting;

import com.turnkey.turnquest.gis.quotation.enums.PartialType;
import com.turnkey.turnquest.gis.quotation.enums.PolicyInterfaceType;
import com.turnkey.turnquest.gis.quotation.enums.YesNo;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class PolicyDto {

    private Long batchNumber;

    private Long foreignId;

    private String code;

    private Long branchId;

    private BigDecimal grossPremium;

    private Long productionDate;

    private String branchCode;

    private boolean readStatus;

    private String policyNumber;

    private String renewalEndorsementNumber;

    private Long agencyId;

    private String agentShortDesc;

    private Long panelId;

    private Long coverTypeId;

    private Long productId;

    private Long quotationId;

    private String coinsurance;

    private Long withEffectFromDate;

    private Long withEffectToDate;

    private BigDecimal totalSumInsured; // Annual sum insured

    private String policyStatus;

    private String clientPolicyNumber;

    private BigDecimal endorsementCommission; // endorsement commission amount

    private String postStatus;

    private String loaded;

    private String debitNoteNo;

    private String authorized;

    private Long currencyId;

    private String currencySymbol;

    private Long clientId;

    private Long phoneNumber;

    private String currentStatus;

    private String binderPolicy;

    private String remarks;

    private String renewable;

    private Long policyCoverToDate;

    private Long policyCoverFromDate;

    private BigDecimal withholding;

    private Long renewalDate;

    private Date renewalNotificationDate;

    private String quotationNo;

    private Long paymentModeId;

    private Long subAgentId;

    private BigDecimal subAgentCommissionAmount;

    private String frequencyCalculation;

    private String frequencyOfPayment;

    private Long paidInstallmentNo;

    private BigDecimal installmentAmount; //payable premium instalment after first installment

    private BigDecimal vatAmt;

    private String policyType;

    private PolicyInterfaceType productInterfaceType = PolicyInterfaceType.CASH;

    private String productCode;

    private Long previousPolicyBatchNumber;

    private BigDecimal coinsuranceShare;

    private String coinsuranceLeader;

    private String premiumComputed;

    private BigDecimal installmentPremium; //next payable premium installment

    private BigDecimal commInstallmentAmount;

    private BigDecimal commInstallmentPremium;

    private BigDecimal outstandingCommission;

    private BigDecimal paidInstallmentAmount;

    private BigDecimal paidInstallmentComm;

    private String reInsuranceReady;

    private BigDecimal nettPremium; //endorsement basic premium

    private BigDecimal butCharge;

    private BigDecimal endorsementMinimumPremium; // from product endorsement minimum premium

    private BigDecimal totalPremium; //annual basic premium + taxes

    private BigDecimal basicPremium;

    private BigDecimal futureAnnualPremium;  //annual basic premium + taxes

    private BigDecimal sumInsured; //endorsement sum insured

    private BigDecimal minimumPremium; //annual minimum premium from product minimum premum

    private BigDecimal commissionAmount; //annual commission amount

    private String populateTaxes;

    private Long organizationId;

    private Long insurerOrgId;

    private String commissionAllowed;

    private String exchangeRateFixed;

    private Long binderId;

    private Long inceptionUnderwritingYear;

    private Long underwritingYearLength;

    private Long cancelDate;

    private BigDecimal commissionLevyAmount;

    private BigDecimal commissionLevyRate;

    private BigDecimal ltaCommissionAmount;

    private BigDecimal trainingLevy;

    private BigDecimal duties;

    private BigDecimal extras;

    private Long phFund; /*restore big decimal*/

    private BigDecimal roadSafetyTax;

    private BigDecimal certchg;

    private BigDecimal motorLevy;

    private BigDecimal clientVat;

    private BigDecimal policyFee;

    private BigDecimal healthTax;

    private BigDecimal ltaWithHoldingTax;

    private String transmittalRefNo;

    private Long receiptId;

    private Boolean sentToRenewal;

    private Long productInstallmentId;

    private YesNo installmentAllowed;

    private BigDecimal outstandingInstallmentAmount;

    private Long nextInstallmentNo;

    private Long paidToDate; // “Cover to” date for the paid instalment

    private Long totalNoOfInstallments;

    private Long maturityDate;  // Next Installment Date E.g If I paid for 1 -31 of Jan the maturity date will be 1st Feb

    private Long installmentPlan;

    private Long parentRevision;

    private boolean renewal;

    private boolean valued;

    private List<PolicyRiskDto> policyRisks;

    private List<PolicyTaxDto> policyTaxes;

    private List<PolicyLevelClauseDto> policyLevelClauses;

    private Long revisionNo;

    private PartialType partialType;

    private Long lastValuationDate;
}
