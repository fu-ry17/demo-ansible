 package com.turnkey.turnquest.gis.quotation.service;

 import com.turnkey.turnquest.gis.quotation.dto.Reports.BooleanResult;
 import com.turnkey.turnquest.gis.quotation.dto.Reports.DebitReportDto;
 import com.turnkey.turnquest.gis.quotation.dto.Reports.QuotationReportDto;
 import com.turnkey.turnquest.gis.quotation.dto.quotation.ResendReportRequestDto;
 import com.turnkey.turnquest.gis.quotation.dto.quotation.ResendReportResponseDto;
 import com.turnkey.turnquest.gis.quotation.enums.QuotationReportType;
 import com.turnkey.turnquest.gis.quotation.model.QuotationReports;

 import java.util.List;

 public interface ReportService {


     /**
      * Generates the quotation summary report
      * @param quotationReportDto
      * @return byte[]
      */

     byte[] generateQuoteSummaryReport(QuotationReportDto quotationReportDto);

     /**
      * Generates the quotation valuation report
      * @param quotationReportDto
      * @return byte[]
      */

     List<byte[]> generateValuationReport(QuotationReportDto quotationReportDto);

     /**
      * Generates the quotation valuation report
      * @param quotationId quotationId
      * @return byte[]
      */

     byte[] generateRenewalNotice(Long quotationId);

     /**
      * Send quotation documents that have been genrated
      * @param quotationId quotationId
      * @return boolean. True if operation succeeded false otherwise
      */

     boolean sendQuotationDocuments(Long quotationId);

     /**
      * Gets the reports for a Converted Quotation
      * @param policyId policy Id or policy batch no
      * @return QuotationReports
      */

     List<QuotationReports> getQuotationReport(Long policyId);

     /**
      * Get the reports for a policy
      * @param policyId the policy Id or policy batch no
      * @return
      */
     List<QuotationReports> getPolicyReports(Long policyId);

     /**
      * Generates Quote Summary,Debit,Receipt and Policy Valuation documents
      * After the endorsement process and creation of the I-Record
      * @param debitReportDto
      */

     void generateDebitReport(DebitReportDto debitReportDto);

     /**
      * Saves reports
      * @param quotationReports quotation report payload
      * @return QuotationReports
      */
     QuotationReports saveQuotationReport(QuotationReports quotationReports);

     /**
      * Check is a report was already generated
      * @param quotationId the quotation Id
      * @param quotationReportType the report type
      * @return Boolean
      */

     BooleanResult reportExists(Long quotationId, QuotationReportType quotationReportType);


     ResendReportResponseDto resendReportsToClient(ResendReportRequestDto request);

 }
