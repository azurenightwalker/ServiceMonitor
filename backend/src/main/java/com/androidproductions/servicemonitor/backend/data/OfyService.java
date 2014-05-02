package com.androidproductions.servicemonitor.backend.data;

import com.androidproductions.servicemonitor.backend.data.models.RegistrationRecord;
import com.androidproductions.servicemonitor.backend.data.models.ServiceGroupRecord;
import com.androidproductions.servicemonitor.backend.data.models.ServiceRecord;
import com.androidproductions.servicemonitor.backend.data.models.SubscriptionRecord;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;

/**
 * Objectify service wrapper so we can statically register our persistence classes
 * More on Objectify here : https://code.google.com/p/objectify-appengine/
 *
 */
public class OfyService {

    static {
        ObjectifyService.register(RegistrationRecord.class);
        ObjectifyService.register(ServiceGroupRecord.class);
        ObjectifyService.register(ServiceRecord.class);
        ObjectifyService.register(SubscriptionRecord.class);
    }

    public static Objectify ofy() {
        return ObjectifyService.ofy();
    }

    public static ObjectifyFactory factory() {
        return ObjectifyService.factory();
    }
}
