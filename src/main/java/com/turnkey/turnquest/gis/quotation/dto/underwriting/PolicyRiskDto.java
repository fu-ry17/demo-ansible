package com.turnkey.turnquest.gis.quotation.dto.underwriting;

import com.turnkey.turnquest.gis.quotation.enums.EndorsementType;
import com.turnkey.turnquest.gis.quotation.enums.ValuationStatus;
import com.turnkey.turnquest.gis.quotation.enums.YesNo;
import jakarta.persistence.Column;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
public class PolicyRiskDto implements Serializable {

    private Long id;

    private String code;

    private Long foreignId;

    private String propertyId;

    private Long previousRiskId;

    private String itemDescription;

    private Long quantity;

    private BigDecimal value;

    private Long withEffectFromDate;

    private Long withEffectToDate;

    private String policyNo;

    private String policyRenewalEndorsementNo;

    private Long policyBatchNumber;

    private BigDecimal basicPremium;

    private BigDecimal netPremium;

    private BigDecimal compulsoryExcess;

    private BigDecimal addTheftExcess;

    private BigDecimal addExpExcess;

    private Long prrRate;

    private BigDecimal compulsoryRetention;

    private BigDecimal policyEstimateMaximumLoss;

    private BigDecimal availableFulcBalance;

    private BigDecimal endorsementDiffAmount;  //endorsment risk premium

    private Long premiumWithEffectFrom;

    private String earthquakeCover;

    private BigDecimal earthquakePremium;

    private String location;

    private BigDecimal itl;

    private Long polinId;

    private Long subClassId;

    private Long productSubClassId;

    private String ncdStatus;

    private String certIssued;

    private Long relatedIpUId;

    private String prorata;

    private BigDecimal bp;

    private BigDecimal grossPremium;  //annual risk premium

    private BigDecimal butCharge;

    private BigDecimal futureAnnualPremium; //annual risk premium + taxes

    private Long previousId;

    private String emlSumInsured;

    private Long reinsured;

    private Long ctCode;

    private String shortDescription;

    private Long quzId;

    private String ippQuzShortDescription;

    private String quzShortDescription;

    private Long nclLevel;

    private Long ncdLevel;

    private Long insuredPropertyId;

    private Long grossCompRetention;

    private Long binderId;

    private BigDecimal commissionAmount; //annual risk commission amount

    private BigDecimal commEndosDiffAmount; //endorsement risk commission amount

    private BigDecimal facreAmount;

    private Long clpCode;

    private Long excessRate;

    private String excessType;

    private String excessRateType;

    private BigDecimal excessMin;

    private BigDecimal excessMax;

    private Long prereqIpuCode;

    private Long escalationType;

    private String endorsementRemove;

    private BigDecimal commissionRate;

    private Long previousPolicyId;

    private Long currencyCode;

    private Long relrCode;

    private String relrShortDescription;

    private BigDecimal reinsuranceAmount;

    private Long insuredId;

    private Long maxExposure;

    private Long commissionRetentionRate;

    private Long effectiveFromDate;

    private Long effectiveToDate;

    private String retroCover;

    private Long retroWithEffectFromDate;

    private Long coverTypeId;

    private String coverTypeCode;

    private BigDecimal sumInsuredDiffAmount; //endorsement risk sum insured

    private Long territoryCode;

    private String territoryDescription;

    private String comments;

    private String fromTime;

    private String toTime;

    private String marriageCertificateNo;

    private BigDecimal coinsurancePremium;

    private BigDecimal totalPremium; /*premium + taxes*/

    private BigDecimal totalValue;

    private Long coverDays;

    private BigDecimal grpCompNetRetention;

    private Long grpSiRiskPercent;

    private Long grpTopLoc;

    private Long grpCompGrossRet;

    private BigDecimal prevPremium; //endorsement risk premium for previous transaction

    private Long currentPrrdCode;

    private BigDecimal totalFap;

    private Long maxDcRefundPercent;

    private BigDecimal extraPremium;

    private String status;

    private Integer underwritingYear;

    private Long transactionEffectiveTo;

    private BigDecimal premiumTax;

    private Integer transactionCount;

    private BigDecimal paidPremium;

    private Long paidTrainingLevy;

    private Integer inceptionUnderwritingYear;

    private String premiumComment;

    private Long coinTransactionLevel;

    private BigDecimal dcPaidPremium;

    private Long dcAp;

    private String itemDetails;

    private String emlBasedOn;

    private Long rcCode;

    private String rcShortDescription;

    private String freeLimit;

    private Long contradIpuCode;

    private Long surveyDate;

    private String declared;

    private Long previousFap;

    private Long previousTotalFap;

    private String minimumPremiumUsed;

    private Long previousRiSumInsured;

    private Long cancellationDays;

    private Long allowedCommisionRate;

    private BigDecimal allowedCommissionAmount;

    private String policyStatus;

    private BigDecimal earthquakePremiumDifference;

    private BigDecimal overrideRiRetention;

    private BigDecimal prevReinsuranceAmount;

    private String riskOtherInterestedParties;

    private BigDecimal phFund;

    private BigDecimal coinPhFund;

    private Long dcPenaltyPercent;

    private String enforceCoverTypeMinimumPremium;

    private BigDecimal prorataSectionPremium;

    private BigDecimal nonProrataSectionPremium;

    private BigDecimal previousProrataSectionPremium;

    private BigDecimal previousNonProrataSectionPrem;

    private BigDecimal previousTotProrataSPremium;

    private BigDecimal previousTotNonProrataSPremium;

    private BigDecimal totProrataSPremium;

    private BigDecimal totNonProrataSPremium;

    private BigDecimal sctButCharge;

    private BigDecimal rskButCharge;

    private BigDecimal rescueCharge;

    private Long rescueServiceId;

    private BigDecimal subAgentCommissionRate;

    private BigDecimal subAgentCommAmount;

    private BigDecimal ltaEndosCommAmount;

    private BigDecimal ltaCommission;

    private BigDecimal longTermAgreementCommissionRate;

    private Long adminFeeDiscRate;

    private BigDecimal adminFeeDiscAmount;

    private Long totalFamilies;

    private Long totalIndividuals;

    private Long totalFemales;

    private Long totalMales;

    private BigDecimal longTermAgreementCommissionDiscountAmount;

    private Long longTermAgreementCommissionDiscountRate;

    private String conveyanceType;

    private BigDecimal stampDuty;

    private BigDecimal marketerCommAMount;

    private BigDecimal marketerCommRate;

    private BigDecimal installPremium;

    private Long vatRate;

    private BigDecimal vatAmount;

    private Integer installmentPeriod;

    private String paymentInstallmentPercentages;

    private BigDecimal nextInstallmentPremium;

    private BigDecimal charge;

    private BigDecimal coinCharge;

    private BigDecimal minPrrAmount;

    private BigDecimal exciseDutyAmount;

    private Long exciseDutyRate;

    private String commissionDiscountType;

    private BigDecimal commissionDiscountRate;

    private String coverSuspended;

    private Long certchg;

    private BigDecimal witholdingTax;

    private String drcrNo;

    private Long extras;

    private Long sd;

    private Long coPhfund;

    private Long clientVatAmount;

    private BigDecimal motorLevy;

    private YesNo installmentAllowed;

    private String frequencyCalculation;

    private String paymentFrequency;

    private Long productInstallmentId;

    private Long paidInstallmentNo;

    private BigDecimal paidInstallmentAmount;

    private BigDecimal installmentAmount; //payable premium instalment after first installment

    private BigDecimal installmentPremium; //next payable premium installment

    private BigDecimal outstandingCommission;

    private BigDecimal paidInstallmentComm;

    private BigDecimal commInstallmentAmount; //payable premium instalment after first installment

    private BigDecimal commInstallmentPremium; //next payable premium installment

    private BigDecimal outstandingInstallmentAmount;

    private Long nextInstallmentNo;

    private Long paidToDate; // “Cover to” date for the paid instalment

    private Long totalNoOfInstallments;

    private Long maturityDate;  // Next Installment Date

    private Long installmentPlan;

    private ValuationStatus valuationStatus;

    private EndorsementType endorsementType;

    private Long valuationNumber;

    private Long valuerOrgId;

    private Long valuerBranchId;

    private List<PolicyRiskSectionDto> policyRiskSections;

    private List<PolicyRiskTaxDto> policyRiskTaxes;

    private List<PolicyClauseDto> policyClauses;

    private PolicyScheduleDto policyRiskSchedules;

    private List<PolicyDocumentsDto> policyDocuments;

    private PolicyValuationDto policyValuationInfo;

    private Long lastValuationDate;

}
