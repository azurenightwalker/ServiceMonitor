package com.androidproductions.servicemonitor.backend.data.models;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

import java.util.Date;

/** The Objectify object model for device registrations we are persisting */
@Entity
public class ServiceRecord {

    @Id
    Long id;

    @Index
    private String serviceId;

    @Index
    private String serviceGroup;

    private int status;

    private long lastUpdate;

    public String getClaimant() {
        return Claimant;
    }

    public boolean isAlerted() {
        return Alerted;
    }

    private String Claimant;

    private boolean Alerted;

    public ServiceRecord() {}

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
        lastUpdate = new Date().getTime();
    }

    public long getLastUpdate() {
        return lastUpdate;
    }

    public String getServiceGroup() {
        return serviceGroup;
    }

    public void setServiceGroup(String serviceGroup) {
        this.serviceGroup = serviceGroup;
    }

    public void setAlerted(boolean alerted) {
        Alerted = alerted;
    }

    public void setClaimant(String claimant) {
        Claimant = claimant;
    }
}