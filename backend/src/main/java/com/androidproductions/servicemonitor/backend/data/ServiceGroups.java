package com.androidproductions.servicemonitor.backend.data;

import com.androidproductions.servicemonitor.backend.data.models.ServiceGroupRecord;
import com.androidproductions.servicemonitor.backend.data.models.SubscriptionRecord;

import java.util.Collection;

import static com.androidproductions.servicemonitor.backend.data.OfyService.ofy;

public class ServiceGroups {
    public static ServiceGroupRecord create(ServiceGroupRecord record) {
        if(exists(record.getName())) {
            return null;
        }
        ofy().save().entity(record).now();
        return record;
    }
    
    public static SubscriptionRecord subscribe(ServiceGroupRecord record, String user)
    {
        SubscriptionRecord sub = new SubscriptionRecord(user,null,record.getName());
        ofy().save().entity(sub).now();
        return sub;
    }

    public static ServiceGroupRecord find(String name) {
        return ofy().load().type(ServiceGroupRecord.class)
                .filter("name",name).first().now();
    }

    public static Collection<ServiceGroupRecord> search() {
        return ofy().load().type(ServiceGroupRecord.class).list();
    }

    public static boolean exists(String name) {
        return find(name) != null;
    }
}
