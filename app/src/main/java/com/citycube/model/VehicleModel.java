package com.citycube.model;

public class VehicleModel {
    String  vehicleId,vehicleName,vehicleSize,weight;
    int vehicleImage;
    boolean chk;

    public VehicleModel(String vehicleId,String vehicleName, String vehicleSize, String weight,int vehicleImage,boolean chk) {
        this.vehicleId = vehicleId;
        this.vehicleName = vehicleName;
        this.vehicleSize = vehicleSize;
        this.weight = weight;
        this.vehicleImage =vehicleImage;
        this.chk =chk;

    }

    public String getVehicleName() {
        return vehicleName;
    }

    public void setVehicleName(String vehicleName) {
        this.vehicleName = vehicleName;
    }

    public String getVehicleSize() {
        return vehicleSize;
    }

    public void setVehicleSize(String vehicleSize) {
        this.vehicleSize = vehicleSize;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public boolean isChk() {
        return chk;
    }

    public void setChk(boolean chk) {
        this.chk = chk;
    }

    public int getVehicleImage() {
        return vehicleImage;
    }

    public void setVehicleImage(int vehicleImage) {
        this.vehicleImage = vehicleImage;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }
}
