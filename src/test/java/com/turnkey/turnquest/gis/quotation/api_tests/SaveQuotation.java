package com.turnkey.turnquest.gis.quotation.api_tests;

import static io.restassured.RestAssured.*;

import com.turnkey.turnquest.gis.quotation.api_tests.utils.Constants;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import static org.hamcrest.Matchers.*;

public class SaveQuotation {

    private String accessToken;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        baseURI = Constants.BASE_URI;
        accessToken = getNewAccessToken();

    }

    private String getNewAccessToken() {
        Response tokenResponse =
                given()
                        .contentType(ContentType.URLENC)
                        .formParam("grant_type", "password")
                        .formParam("client_id", "web-client")
                        .formParam("client_secret", "62f8c3fb-0fba-4358-a8da-80b52dd1e32b")
                        .formParam("username", "eva.mutuku@agencify.insure")
                        .formParam("password", "123456789")
                        .formParam("scope", "email profile")
                        .post(Constants.AUTH_URI);

        return tokenResponse.jsonPath().getString("access_token");
    }


    private String generateRandomQuotationNo() {
        // You can change the format as needed
        String uniquePart = String.valueOf(System.currentTimeMillis()); // Unique timestamp
        return "Q/HQ/" + uniquePart + "/Sep/24"; // Adjust month and year as needed
    }


    @Test
    public void testPostSaveQuotation() {

        // Generating a random quotation number
        String randomQuotationNo = generateRandomQuotationNo();

        // The request body
        String requestBody = "{\n" +
                "    \"createdDate\": 1724055037471,\n" +
                "    \"modifiedDate\": 1724055038064,\n" +
                "    \"createdBy\": \"Eva Mutuku \",\n" +
                "    \"modifiedBy\": \"Eva Mutuku \",\n" +
                "    \"id\": null,\n" +
                "    \"renewalBatchNo\": null,\n" +
                "    \"quotationNo\": \"" + randomQuotationNo + "\",\n" +
                "    \"clientId\": null,\n" +
                "    \"agencyId\": null,\n" +
                "    \"insurerOrgId\": 9784005,\n" +
                "    \"panelId\": 11,\n" +
                "    \"branchId\": null,\n" +
                "    \"currencyId\": 35,\n" +
                "    \"coverToDate\": 1755018400605,\n" +
                "    \"coverFromDate\": 1723507200000,\n" +
                "    \"totalSumInsured\": 1000000.00,\n" +
                "    \"comments\": null,\n" +
                "    \"currentStatus\": \"D\",\n" +
                "    \"status\": \"NB\",\n" +
                "    \"grossPremium\": 50000.00,\n" +
                "    \"premium\": 50000.00,\n" +
                "    \"basicPremium\": 50000.00,\n" +
                "    \"installmentPremium\": null,\n" +
                "    \"installmentCommission\": null,\n" +
                "    \"commissionAmount\": -5000.00,\n" +
                "    \"productionDate\": null,\n" +
                "    \"paymentFrequency\": \"A\",\n" +
                "    \"currencyRate\": null,\n" +
                "    \"currencySymbol\": \"KES\",\n" +
                "    \"webPolicyId\": null,\n" +
                "    \"policyNo\": null,\n" +
                "    \"policyId\": null,\n" +
                "    \"insuredCode\": null,\n" +
                "    \"organizationId\": 4000,\n" +
                "    \"withholdingTax\": -500.00,\n" +
                "    \"paymentRef\": null,\n" +
                "    \"readStatus\": false,\n" +
                "    \"renewal\": false,\n" +
                "    \"underwritingYear\": 2024,\n" +
                "    \"isProposalConsented\": false,\n" +
                "    \"lastValuationDate\": null,\n" +
                "    \"quotationProducts\": [\n" +
                "        {\n" +
                "            \"createdDate\": 1724055037488,\n" +
                "            \"modifiedDate\": 1724055037534,\n" +
                "            \"createdBy\": \"Eva Mutuku \",\n" +
                "            \"modifiedBy\": \"Eva Mutuku \",\n" +
                "            \"id\": null,\n" +
                "            \"code\": null,\n" +
                "            \"foreignId\": null,\n" +
                "            \"productId\": 4450,\n" +
                "            \"agentId\": null,\n" +
                "            \"panelId\": null,\n" +
                "            \"productGroupId\": 111,\n" +
                "            \"productShortDescription\": null,\n" +
                "            \"policyCoverFrom\": 1723507200000,\n" +
                "            \"policyCoverTo\": 1755018400605,\n" +
                "            \"withEffectFromDate\": 1723507200000,\n" +
                "            \"withEffectToDate\": 1755018400605,\n" +
                "            \"quotationId\": 1546,\n" +
                "            \"quotationNo\": \"Q/HQ/0001/Aug/24\",\n" +
                "            \"currencySymbol\": \"KES\",\n" +
                "            \"quotationRevisionNumber\": null,\n" +
                "            \"totalSumInsured\": 1000000.00,\n" +
                "            \"totalPremium\": 50000.00,\n" +
                "            \"basicPremium\": 50000.00,\n" +
                "            \"remarks\": null,\n" +
                "            \"futureAnnualPremium\": 50000.00,\n" +
                "            \"status\": null,\n" +
                "            \"fromBinder\": null,\n" +
                "            \"binderId\": null,\n" +
                "            \"agentCode\": null,\n" +
                "            \"converted\": \"Y\",\n" +
                "            \"marketerCommissionAmount\": null,\n" +
                "            \"commissionAmount\": -5000.00,\n" +
                "            \"withHoldingTax\": 500.00,\n" +
                "            \"subAgentCommissionAmount\": null,\n" +
                "            \"longTermArrangementCommissionAmount\": null,\n" +
                "            \"isQuickQuote\": null,\n" +
                "            \"installmentAllowed\": \"N\",\n" +
                "            \"frequencyCalculation\": null,\n" +
                "            \"paymentFrequency\": null,\n" +
                "            \"productInstallmentId\": null,\n" +
                "            \"paidInstallmentNo\": 0,\n" +
                "            \"paidInstallmentAmount\": 0.00,\n" +
                "            \"installmentAmount\": 0.00,\n" +
                "            \"installmentPremium\": 0.00,\n" +
                "            \"outstandingCommission\": -5000.00,\n" +
                "            \"paidInstallmentComm\": 0.00,\n" +
                "            \"commInstallmentAmount\": 0.00,\n" +
                "            \"commInstallmentPremium\": 0.00,\n" +
                "            \"outstandingInstallmentAmount\": 50000.00,\n" +
                "            \"nextInstallmentNo\": 1,\n" +
                "            \"paidToDate\": null,\n" +
                "            \"totalNoOfInstallments\": 0,\n" +
                "            \"maturityDate\": 1755018400605,\n" +
                "            \"installmentPlan\": null,\n" +
                "            \"renewalNotificationDate\": null,\n" +
                "            \"lastValuationDate\": null,\n" +
                "            \"quotationRisks\": [\n" +
                "                {\n" +
                "                    \"createdDate\": 1724055037512,\n" +
                "                    \"modifiedDate\": 1724055038051,\n" +
                "                    \"createdBy\": null,\n" +
                "                    \"modifiedBy\": \"Eva Mutuku \",\n" +
                "                    \"id\": null,\n" +
                "                    \"code\": null,\n" +
                "                    \"foreignId\": null,\n" +
                "                    \"policyRiskId\": null,\n" +
                "                    \"quotationProductId\": 1486,\n" +
                "                    \"binderId\": 1072,\n" +
                "                    \"riskId\": \"KCF 876C\",\n" +
                "                    \"itemDescription\": \"Mercedes-Benz/Actros/Truck\",\n" +
                "                    \"certificateNo\": null,\n" +
                "                    \"quotationRevisionNumber\": 0.00,\n" +
                "                    \"clientType\": \"I\",\n" +
                "                    \"quantity\": null,\n" +
                "                    \"withEffectFromDate\": 1723507200000,\n" +
                "                    \"withEffectToDate\": 1755018400605,\n" +
                "                    \"coverTypeId\": 1,\n" +
                "                    \"coverTypeCode\": \"COMP\",\n" +
                "                    \"subClassId\": 74,\n" +
                "                    \"productSubClassId\": 453,\n" +
                "                    \"clientId\": null,\n" +
                "                    \"totalPremium\": 50000.00,\n" +
                "                    \"basicPremium\": 50000.00,\n" +
                "                    \"annualPremium\": null,\n" +
                "                    \"futureAnnualPremium\": 50000.00,\n" +
                "                    \"value\": 1000000.00,\n" +
                "                    \"minimumPremiumUsed\": null,\n" +
                "                    \"coverDays\": null,\n" +
                "                    \"ncdLevel\": null,\n" +
                "                    \"commissionRate\": 10.00,\n" +
                "                    \"commissionAmount\": -5000.00,\n" +
                "                    \"longTermAgreementCommissionRate\": 0.00,\n" +
                "                    \"longTermAgreementCommissionAmount\": 0.00,\n" +
                "                    \"subAgentCommissionRate\": null,\n" +
                "                    \"subAgentCommissionAmount\": null,\n" +
                "                    \"enforceCoverTypeMinimumPremium\": null,\n" +
                "                    \"marketerCommissionAmount\": null,\n" +
                "                    \"marketerCommissionRate\": null,\n" +
                "                    \"withHoldingTax\": 500.00,\n" +
                "                    \"valuationStatus\": \"OPEN\",\n" +
                "                    \"endorsementType\": \"NONE\",\n" +
                "                    \"installmentAllowed\": \"N\",\n" +
                "                    \"frequencyCalculation\": null,\n" +
                "                    \"paymentFrequency\": null,\n" +
                "                    \"productInstallmentId\": null,\n" +
                "                    \"paidInstallmentNo\": null,\n" +
                "                    \"paidInstallmentAmount\": 0.00,\n" +
                "                    \"installmentAmount\": 0.00,\n" +
                "                    \"installmentPremium\": 0.00,\n" +
                "                    \"outstandingCommission\": 5000.00,\n" +
                "                    \"paidInstallmentComm\": 0.00,\n" +
                "                    \"commInstallmentAmount\": 0.00,\n" +
                "                    \"commInstallmentPremium\": 0.00,\n" +
                "                    \"outstandingInstallmentAmount\": 0.00,\n" +
                "                    \"nextInstallmentNo\": 1,\n" +
                "                    \"paidToDate\": null,\n" +
                "                    \"totalNoOfInstallments\": 0,\n" +
                "                    \"maturityDate\": null,\n" +
                "                    \"butCharge\": null,\n" +
                "                    \"installmentPlan\": null,\n" +
                "                    \"valuerOrgId\": null,\n" +
                "                    \"valuerBranchId\": null,\n" +
                "                    \"lastValuationDate\": null,\n" +
                "                    \"motorSchedules\": {\n" +
                "                        \"createdDate\": 1724055038049,\n" +
                "                        \"modifiedDate\": 1724055038049,\n" +
                "                        \"createdBy\": \"Eva Mutuku \",\n" +
                "                        \"modifiedBy\": \"Eva Mutuku \",\n" +
                "                        \"id\": null,\n" +
                "                        \"ipuCode\": null,\n" +
                "                        \"riskId\": \"KCF 876C\",\n" +
                "                        \"make\": \"Mercedes-Benz\",\n" +
                "                        \"cubicCapacity\": null,\n" +
                "                        \"yearOfManufacture\": 2024,\n" +
                "                        \"yearOfRegistration\": null,\n" +
                "                        \"carryCapacity\": 8,\n" +
                "                        \"value\": null,\n" +
                "                        \"bodyType\": \"Truck\",\n" +
                "                        \"chasisNo\": null,\n" +
                "                        \"engineNo\": null,\n" +
                "                        \"color\": null,\n" +
                "                        \"logbook\": null,\n" +
                "                        \"tonnage\": null,\n" +
                "                        \"noPrint\": 0,\n" +
                "                        \"certificateNo\": null,\n" +
                "                        \"certificateType\": null,\n" +
                "                        \"model\": \"Actros\",\n" +
                "                        \"organizationId\": null\n" +
                "                    },\n" +
                "                    \"quoteDocument\": [],\n" +
                "                    \"quotationRiskSections\": [\n" +
                "                        {\n" +
                "                            \"createdDate\": 1724055037521,\n" +
                "                            \"modifiedDate\": 1724055038057,\n" +
                "                            \"createdBy\": null,\n" +
                "                            \"modifiedBy\": \"Eva Mutuku \",\n" +
                "                            \"id\": null,\n" +
                "                            \"description\": \"SUM INSURED\",\n" +
                "                            \"foreignId\": null,\n" +
                "                            \"sectionId\": 1,\n" +
                "                            \"sectionCode\": null,\n" +
                "                            \"subClassSectionId\": 267,\n" +
                "                            \"subClassSectionDesc\": \"SUM INSURED\",\n" +
                "                            \"sectionType\": \"SS\",\n" +
                "                            \"quotationRiskId\": 1470,\n" +
                "                            \"limitAmount\": 1000000.00,\n" +
                "                            \"annualPremiumAmount\": null,\n" +
                "                            \"premiumAmount\": 50000.00,\n" +
                "                            \"minimumPremiumAmount\": 0.00,\n" +
                "                            \"usedLimitAmount\": null,\n" +
                "                            \"freeLimitAmount\": 0.00,\n" +
                "                            \"premiumRate\": 5.00,\n" +
                "                            \"multiplierRate\": 1.00,\n" +
                "                            \"multiplierDivisionFactor\": 1.00,\n" +
                "                            \"rateDivisionFactor\": 100.00,\n" +
                "                            \"rateType\": \"FXD\",\n" +
                "                            \"prorated\": \"P\",\n" +
                "                            \"premiumRateDescription\": \"Percent\",\n" +
                "                            \"calculationGroup\": null,\n" +
                "                            \"rowNumber\": null,\n" +
                "                            \"compute\": null,\n" +
                "                            \"benefitType\": \"LIMIT\",\n" +
                "                            \"dualBasis\": null,\n" +
                "                            \"sumInsuredRate\": null,\n" +
                "                            \"sumInsuredLimitType\": null,\n" +
                "                            \"sectionMandatory\": null,\n" +
                "                            \"section\": null\n" +
                "                        }\n" +
                "                    ],\n" +
                "                    \"quotationRiskTaxes\": [],\n" +
                "                    \"quotationRiskClauses\": [],\n" +
                "                    \"coverType\": null\n" +
                "                }\n" +
                "            ],\n" +
                "            \"quotationProductTaxes\": []\n" +
                "        }\n" +
                "    ],\n" +
                "    \"client\": null,\n" +
                "    \"organization\": {\n" +
                "        \"id\": 9785187,\n" +
                "        \"entityId\": 9785186,\n" +
                "        \"accountTypeId\": 7,\n" +
                "        \"accountType\": {\n" +
                "            \"id\": 7,\n" +
                "            \"code\": \"I\",\n" +
                "            \"accountType\": \"INSURANCE COMPANY\",\n" +
                "            \"typeId\": \"I\",\n" +
                "            \"withHoldingTaxRate\": 10.00,\n" +
                "            \"commissionReservedRate\": 0,\n" +
                "            \"maxAdvanceAmount\": null,\n" +
                "            \"maxAdvanceRepaymentAmount\": null,\n" +
                "            \"receiptsIncludeCommission\": \"N\",\n" +
                "            \"overrideRate\": 0,\n" +
                "            \"idSerialFormat\": \"I[SERIALNO]\",\n" +
                "            \"vatRate\": null,\n" +
                "            \"format\": \"0020[SERIALNO]\",\n" +
                "            \"odlCode\": null,\n" +
                "            \"noGenCode\": null,\n" +
                "            \"typeShortDescriptionBkp\": null,\n" +
                "            \"commLevyRate\": null,\n" +
                "            \"exciseDutyRate\": 0,\n" +
                "            \"commissionLevyRate\": 16,\n" +
                "            \"creditPolicyApplicable\": null,\n" +
                "            \"ltaWithholdingTaxRate\": null,\n" +
                "            \"nextNo\": 14,\n" +
                "            \"suffix\": null\n" +
                "        },\n" +
                "        \"entities\": {\n" +
                "            \"id\": 9785186,\n" +
                "            \"imageUrl\": \"https://takafulafrica.co.ke/wp-content/uploads/2024/03/takaful-insurance-of-africa-logo.png\",\n" +
                "            \"iraRegistrationNumber\": null,\n" +
                "            \"firstName\": null,\n" +
                "            \"lastName\": null,\n" +
                "            \"organizationName\": \"TAKAFUL INSURANCE\",\n" +
                "            \"emailAddress\": \"TalkToUs@takafulafrica.co.ke\",\n" +
                "            \"phoneNumber\": \"+254 703 808010\",\n" +
                "            \"physicalAddress\": \"Head Office, Renaissance Corporate Park,\\nElgon Road, Upperhill,\",\n" +
                "            \"akiMemberCompanyId\": 40,\n" +
                "            \"akiIntegrated\": false\n" +
                "        }\n" +
                "    },\n" +
                "    \"agent\": {\n" +
                "        \"id\": 602,\n" +
                "        \"panelId\": null,\n" +
                "        \"insurerId\": 9785187,\n" +
                "        \"organizationId\": 9784005,\n" +
                "        \"code\": \"TAK432\",\n" +
                "        \"agentLicenceNo\": null,\n" +
                "        \"withHoldingTax\": null\n" +
                "    },\n" +
                "    \"deal\": null\n" +
                "}";

        Response response =
                given()
                        .auth().oauth2(accessToken)
                        .contentType(ContentType.JSON)
                        .body(requestBody).
                when()
                        .post("gis/quotation/quotations/save-quick-quote").
                then()
                        .statusCode(200)
                        .body("quotationNo", equalTo(randomQuotationNo))
                        .body("totalSumInsured", equalTo(1000000.00F))
                        .body("grossPremium", equalTo(50000.00F))
                        .body("premium", equalTo(50000.00F))
                        .body("basicPremium", equalTo(50000.00F))
                        .body("quotationProducts", hasSize(1))
                        .body("quotationProducts[0].quotationRisks", hasSize(1))
                        .body("quotationProducts[0].quotationRisks[0].quotationRiskSections", hasSize(1))
                        .body("quotationProducts[0].quotationProductTaxes", hasSize(0))
                        .body("client", equalTo(null)) // Assuming it's a null value
                        .extract().response();



        System.out.println("Response: " + response.asString());

    }
}
