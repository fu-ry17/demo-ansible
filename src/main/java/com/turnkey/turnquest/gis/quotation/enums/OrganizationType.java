package com.turnkey.turnquest.gis.quotation.enums;

public enum OrganizationType {
    INSURANCE("INS"),
    BROKER("BRK");

    private String literal;

    private OrganizationType(String literal) {

        this.literal = literal;
    }

    public String getLiteral() {
        return literal;
    }

    public void setLiteral(String literal) {
        this.literal = literal;
    }
}
