package com.androidproductions.servicemonitor.backend.data;


import com.androidproductions.servicemonitor.backend.data.models.ServiceGroupRecord;
import com.androidproductions.servicemonitor.backend.data.models.ServiceRecord;
import com.androidproductions.servicemonitor.backend.data.models.SubscriptionRecord;

import java.util.ArrayList;
import java.util.Collection;

import static com.androidproductions.servicemonitor.backend.data.OfyService.ofy;

public class Services {
    public static ServiceRecord find(String serviceId, String serviceGroup) {
        return ofy().load().type(ServiceRecord.class)
                .filter("serviceId", serviceId)
                .filter("serviceGroup", serviceGroup)
                .first().now();
    }

    public static Collection<ServiceRecord> search(String serviceGroup) {
        return ofy().load().type(ServiceRecord.class)
                .filter("serviceGroup", serviceGroup)
                .list();
    }

    public static int delete(String serviceId, String serviceGroup) {
        Collection<ServiceRecord> toDel = new ArrayList<ServiceRecord>();
        if (serviceId == null)
            toDel = search(serviceGroup);
        else
            toDel.add(find(serviceId, serviceGroup));
        for(ServiceRecord ser : toDel)
        {
            ofy().delete().entity(ser).now();
        }
        return toDel.size();
    }

    public static ServiceRecord update(String serviceId, String serviceGroup, ServiceRecord newSr) {
        ServiceRecord old = find(serviceId, serviceGroup);
        if (old == null)
            return null;

        old.setStatus(newSr.getStatus());
        old.setClaimant(newSr.getClaimant());
        old.setAlerted(newSr.isAlerted());
        ofy().save().entity(old).now();

        return old;
    }

    public static ServiceRecord create(ServiceRecord record) {
        if (!ServiceGroups.exists(record.getServiceGroup()))
            return null;

        if(find(record.getServiceId(), record.getServiceGroup()) != null) {
            return null;
        }
        // Trigger timestamp update
        record.setStatus(record.getStatus());
        ofy().save().entity(record).now();
        return record;
    }

    public static SubscriptionRecord subscribe(ServiceRecord record, String user)
    {
        SubscriptionRecord sub = new SubscriptionRecord(user,record.getServiceId(),record.getServiceGroup());
        ofy().save().entity(sub).now();
        return sub;
    }
}
