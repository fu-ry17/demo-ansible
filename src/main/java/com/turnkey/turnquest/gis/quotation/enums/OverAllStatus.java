package com.turnkey.turnquest.gis.quotation.enums;

public enum OverAllStatus {
    OPEN("open"),CLOSED("closed");

    private String state;

    OverAllStatus(String state){
        this.state = state;
    }

    public String getState(){
        return state;
    }

    @Override
    public String toString() {
        return getState();
    }
}
