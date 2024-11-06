package com.turnkey.turnquest.gis.quotation.aki.enums;

public enum CertificateType {

    TYPE_A("Type A"),
    TYPE_B("Type B"),
    TYPE_C("Type C"),
    TYPE_D("Type D");

    private final String name;

    CertificateType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
