package com.example.testwasalnyuser.trip_node;

import java.io.Serializable;

public class TripNode implements Serializable {
    private String currentStatus;
    private String distanceLengthStr;
    private String driverId;
    private String durationStr;
    private Long fareEstimate;
    private DistinationCoordinates distinationCoordinates;
    private DriverInfo driverInfo;
    private VehicleInfo vehicleInfo;

    public String getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
    }

    public String getDistanceLengthStr() {
        return distanceLengthStr;
    }

    public void setDistanceLengthStr(String distanceLengthStr) {
        this.distanceLengthStr = distanceLengthStr;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getDurationStr() {
        return durationStr;
    }

    public void setDurationStr(String durationStr) {
        this.durationStr = durationStr;
    }

    public Long getFareEstimate() {
        return fareEstimate;
    }

    public void setFareEstimate(Long fareEstimate) {
        this.fareEstimate = fareEstimate;
    }

    public DistinationCoordinates getDistinationCoordinates() {
        return distinationCoordinates;
    }

    public void setDistinationCoordinates(DistinationCoordinates distinationCoordinates) {
        this.distinationCoordinates = distinationCoordinates;
    }

    public DriverInfo getDriverInfo() {
        return driverInfo;
    }

    public void setDriverInfo(DriverInfo driverInfo) {
        this.driverInfo = driverInfo;
    }

    public VehicleInfo getVehicleInfo() {
        return vehicleInfo;
    }

    public void setVehicleInfo(VehicleInfo vehicleInfo) {
        this.vehicleInfo = vehicleInfo;
    }
}
