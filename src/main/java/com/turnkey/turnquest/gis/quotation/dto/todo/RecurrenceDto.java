package com.turnkey.turnquest.gis.quotation.dto.todo;

import com.turnkey.turnquest.gis.quotation.enums.Frequency;
import lombok.Data;

@Data
public class RecurrenceDto {

    private Long id;

    private Frequency frequency;

    private String count;

    private Long startDate;

    private Long time;

    private Long[] weekDay;

    private Long dayOfMonth;

    private Long timesInMonth;

    private Long startMonth;

}
