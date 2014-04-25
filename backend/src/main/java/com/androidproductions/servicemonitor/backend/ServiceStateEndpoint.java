package com.androidproductions.servicemonitor.backend;

import com.google.android.gcm.server.Message;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.CollectionResponse;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import javax.inject.Named;

import static com.androidproductions.servicemonitor.backend.OfyService.ofy;

@Api(name = "services", version = "v1.2", namespace = @ApiNamespace(ownerDomain = "backend.servicemonitor.androidproductions.com", ownerName = "backend.servicemonitor.androidproductions.com", packagePath=""))
public class ServiceStateEndpoint {

    private static final Logger log = Logger.getLogger(ServiceStateEndpoint.class.getName());

    private static final int AlertLevel = 1<<2;

    /**
     * Register a service to the backend
     *
     * @param serviceId The Service name to add
     */
    @ApiMethod(name = "register")
    public void registerService(@Named("serviceId") String serviceId, @Named("serviceGroup") String serviceGroup) {
        if(findRecord(serviceId, serviceGroup) != null) {
            log.info("Service " + serviceId + " already registered, skipping register");
            return;
        }
        ServiceRecord record = new ServiceRecord();
        record.setServiceId(serviceId);
        record.setServiceGroup(serviceGroup);
        record.setStatus(1);
        ofy().save().entity(record).now();
    }

    /**
     * Update a service in the backend
     *
     * @param serviceId The Service name to add
     * @param status The new service status
     */
    @ApiMethod(name = "update")
    public void updateService(@Named("serviceId") String serviceId, @Named("serviceGroup") String serviceGroup, @Named("status") int status) {
        if(findRecord(serviceId,serviceGroup) == null) {
            log.info("Service " + serviceId + " not registered, skipping update");
            return;
        }

        ServiceRecord record = findRecord(serviceId, serviceGroup);
        record.setStatus(status);
        ofy().save().entity(record).now();

        if (status >= AlertLevel)
        {
            Message.Builder builder = new Message.Builder();
            builder.addData("serviceId", record.getServiceId());
            builder.addData("serviceGroup", record.getServiceGroup());
            builder.addData("status", String.valueOf(record.getStatus()));
            builder.addData("time", String.valueOf(record.getLastUpdate()));
            Message msg = builder.build();
            try {
                GcmHelper.SendMessage(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Unregister a service from the backend
     *
     * @param serviceId The service Id to remove
     */
    @ApiMethod(name = "unregister")
    public void unregisterService(@Named("serviceId") String serviceId, @Named("serviceGroup") String serviceGroup) {
        ServiceRecord record = findRecord(serviceId,serviceGroup);
        if(record == null) {
            log.info("Service " + serviceId + " not registered, skipping unregister");
            return;
        }
        ofy().delete().entity(record).now();
    }

    /**
     * Return a collection of registered services
     *
     * @param serviceGroup The group of services to list
     * @return a list of services
     */
    @ApiMethod(name = "listServices")
    public CollectionResponse<ServiceRecord> listServices(@Named("serviceGroup") String serviceGroup) {
        List<ServiceRecord> records = ofy().transactionless().load().type(ServiceRecord.class)
                .filter("serviceGroup", serviceGroup).list();
        return CollectionResponse.<ServiceRecord>builder().setItems(records).build();
    }

    private ServiceRecord findRecord(String serviceId, String serviceGroup) {
        return ofy().load().type(ServiceRecord.class)
                .filter("serviceId", serviceId)
                .filter("serviceGroup", serviceGroup)
                .first().now();
    }

}