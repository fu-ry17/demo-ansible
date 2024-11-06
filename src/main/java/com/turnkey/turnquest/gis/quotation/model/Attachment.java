package com.turnkey.turnquest.gis.quotation.model;

import com.turnkey.turnquest.gis.quotation.model.audit.BaseAudit;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class Attachment extends BaseAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "attachment-seq")
    @SequenceGenerator(name="attachment-seq", sequenceName = "attachment_seq", allocationSize = 1)
    private long id;

    private String mimeType;

    private String fileName;

    private String newFileName;

    private long directoryId;

    private long organizationId;

}
