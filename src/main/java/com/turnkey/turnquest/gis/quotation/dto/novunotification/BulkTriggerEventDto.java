package com.turnkey.turnquest.gis.quotation.dto.novunotification;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @author Titus Murithi Bundi
 */
@NoArgsConstructor
@Data
public class BulkTriggerEventDto {
    String workflowId;
    Map<String, String> payload;
    List<Topic> to;
}
