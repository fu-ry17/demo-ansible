package com.turnkey.turnquest.gis.quotation.enums;

public enum ProrationType {
    P("P"),
    F("F");

    private String literal;

    private ProrationType(String literal) {

        this.literal = literal;
    }

    public String getLiteral() {
        return literal;
    }

    public void setLiteral(String literal) {
        this.literal = literal;
    }
}
