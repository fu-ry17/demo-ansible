package com.turnkey.turnquest.gis.quotation.dto.todo;

import lombok.Data;

import java.util.List;

@Data
public class TodoDto {

    private Long id;

    private String note;

    private String actionType;

    private Long actionDate;

    private String status;

    private Long organizationId;

    private Long dealId;

    private List<TodoLinkDto> todoLinks;

    private List<AttachmentDto> attachments;

}
