package com.androidproductions.servicemonitor.backend.data.models;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

/** The Objectify object model for device registrations we are persisting */
@Entity
public class RegistrationRecord {

    @Id
    Long id;

    @Index
    private String regId;

    public RegistrationRecord(String regId) {
        this.regId = regId;
    }
    // you can add more fields...

    public String getUser() {
        return User;
    }

    public void setUser(String user) {
        User = user;
    }

    private String User;

    public RegistrationRecord() {}

    public String getRegId() {
        return regId;
    }

    public void setRegId(String regId) {
        this.regId = regId;
    }

    public Long getId() {
        return id;
    }
}