package com.turnkey.turnquest.gis.quotation.enums;

public enum PolicyInterfaceType {
    CASH("CASH"),
    ACCRUAL("ACCRUAL");

    private final String name;

    PolicyInterfaceType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
