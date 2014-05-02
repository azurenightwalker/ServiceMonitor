package com.androidproductions.servicemonitor.backend.data.models;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

/** The Objectify object model for device registrations we are persisting */
@Entity
public class SubscriptionRecord {

    @Id
    Long id;

    @Index
    private String user;

    @Index
    private String serviceId;

    @Index
    private String serviceGroup;

    public SubscriptionRecord() { }
    public SubscriptionRecord(String user, String serviceId, String serviceGroup) {
        this.user = user;
        this.serviceId = serviceId;
        this.serviceGroup = serviceGroup;
    }


    public String getServiceId() {
        return serviceId;
    }

    public String getServiceGroup() {
        return serviceGroup;
    }

    public String getUser() {
        return user;
    }


    public void setUser(String user) {
        this.user = user;
    }
}