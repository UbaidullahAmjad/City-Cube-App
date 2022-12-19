package com.citycube.model;

public class NumberPassengerModel {
    String id,passengerNumber;
    boolean chk;


    public NumberPassengerModel(String id, String passengerNumber, boolean chk) {
        this.id = id;
        this.passengerNumber = passengerNumber;
        this.chk = chk;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassengerNumber() {
        return passengerNumber;
    }

    public void setPassengerNumber(String passengerNumber) {
        this.passengerNumber = passengerNumber;
    }

    public boolean isChk() {
        return chk;
    }

    public void setChk(boolean chk) {
        this.chk = chk;
    }
}
