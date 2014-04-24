package com.androidproductions.servicemonitor.backend;

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

    private int Status;

    private long lastUpdate;
    // you can add more fields...

    public ServiceRecord() {}

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
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
}