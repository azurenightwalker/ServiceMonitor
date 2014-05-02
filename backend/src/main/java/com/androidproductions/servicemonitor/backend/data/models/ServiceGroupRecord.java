package com.androidproductions.servicemonitor.backend.data.models;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

import java.util.Date;

/** The Objectify object model for device registrations we are persisting */
@Entity
public class ServiceGroupRecord {

    @Id
    Long id;

    @Index
    private String name;

    public String getName() {
        return name;
    }


}