package com.androidproductions.servicemonitor.backend.data;

import com.androidproductions.servicemonitor.backend.data.models.SubscriptionRecord;
import com.googlecode.objectify.cmd.LoadType;

import java.util.Collection;

import static com.androidproductions.servicemonitor.backend.data.OfyService.ofy;

public class Subscriptions {
    public static Collection<SubscriptionRecord> search(String userId) {
        LoadType<SubscriptionRecord> data = ofy().load().type(SubscriptionRecord.class);
        if (userId == null)
            return data.list();
        return data.filter("user",userId).list();
    }

    public static SubscriptionRecord find(String userId, String subscriptionId) {
        return ofy().load().type(SubscriptionRecord.class)
                .filter("user", userId)
                .filter("Id", subscriptionId)
                .first().now();
    }

    public static SubscriptionRecord create(SubscriptionRecord record) {
        ofy().save().entity(record).now();
        return record;
    }

    public static int delete(String userId, String subscriptionId) {
        SubscriptionRecord sr = find(userId, subscriptionId);
        if (sr == null)
            return 0;
        ofy().delete().entity(sr).now();
        return 1;
    }
}
