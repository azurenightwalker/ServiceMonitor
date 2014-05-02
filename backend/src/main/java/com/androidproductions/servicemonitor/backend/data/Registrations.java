package com.androidproductions.servicemonitor.backend.data;

import com.androidproductions.servicemonitor.backend.data.models.RegistrationRecord;

import java.util.Collection;

import static com.androidproductions.servicemonitor.backend.data.OfyService.ofy;

public class Registrations {
    public static RegistrationRecord create(RegistrationRecord record) {
        if(find(record.getRegId()) != null) {
            return null;
        }
        ofy().save().entity(record).now();
        return record;
    }

    public static int delete(String registrationId) {
        RegistrationRecord rec = find(registrationId);
        if(rec != null) {
            ofy().delete().entity(rec).now();
            return 1;
        }
        return 0;
    }

    public static RegistrationRecord find(String regId) {
        return ofy().load().type(RegistrationRecord.class).filter("regId", regId).first().now();
    }

    public static Collection<RegistrationRecord> search() {
        return ofy().load().type(RegistrationRecord.class).list();
    }
}
