package com.turnkey.turnquest.gis.quotation.aki.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CoverTypeCode {

    COMP(100,"Comprehensive"),
    TPO(200,"Third Party Only"),
    TPFT(300,"Third-party, Theft and Fire"),
    STD(100,"STANDARD"),
    STANDARD(100,"STD"),
    CAPITAL(400,"CAPITAL"),
    BCBURG(500, "BCBURG"),
    BCFIRE(600, "BCFIRE"),
    EARN(700, "EARN");

    private int code;

    private String description;

}
