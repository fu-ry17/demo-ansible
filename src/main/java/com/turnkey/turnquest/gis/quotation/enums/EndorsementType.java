package com.turnkey.turnquest.gis.quotation.enums;

public enum EndorsementType {
    SUM_INSURED("SI"),
    REVISION("REV"),
    NONE("N");

    private String status;

    EndorsementType(String status) {
        this.status = status;
    }

    private String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return getStatus();
    }
}
