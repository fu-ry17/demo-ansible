package com.turnkey.turnquest.gis.quotation.model;

import com.turnkey.turnquest.gis.quotation.enums.QuotationReportType;
import com.turnkey.turnquest.gis.quotation.model.audit.BaseAudit;
import lombok.*;

import jakarta.persistence.*;

@Builder
@EqualsAndHashCode(callSuper = false)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuotationReports extends BaseAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "quot-reports-seq")
    @SequenceGenerator(name="quot-reports-seq", sequenceName = "QUOTATION_REPORTS_SEQ", allocationSize = 1)
    private Long id;

    private Long quotationId;

    private Long policyId;

    private String policyNumber;

    private String mimeType;

    private int fileSize;

    private String fileName;

    @Builder.Default
    private boolean mailable = true;

    @Enumerated(EnumType.STRING)
    private QuotationReportType fileCategory;

    private String fileUrl;

    private Long organizationId;

    private String quotationStatus;

    private String policyCurrentStatus;

    private boolean sent = false;

    @Builder.Default
    @Column(columnDefinition = "INT DEFAULT 0")
    private int emailRetryCount = 0;

    @Builder.Default
    @Column(columnDefinition = "INT DEFAULT 3")
    private int maxRetryCount = 3;

    @Column(columnDefinition = "TEXT NULL")
    private String error;

}
