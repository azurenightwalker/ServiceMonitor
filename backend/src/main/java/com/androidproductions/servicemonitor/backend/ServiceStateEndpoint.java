package com.androidproductions.servicemonitor.backend;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.CollectionResponse;

import java.util.List;
import java.util.logging.Logger;

import javax.inject.Named;

import static com.androidproductions.servicemonitor.backend.OfyService.ofy;

@Api(name = "services", version = "v1.1", namespace = @ApiNamespace(ownerDomain = "backend.servicemonitor.androidproductions.com", ownerName = "backend.servicemonitor.androidproductions.com", packagePath=""))
public class ServiceStateEndpoint {

    private static final Logger log = Logger.getLogger(ServiceStateEndpoint.class.getName());

    /**
     * Register a service to the backend
     *
     * @param serviceId The Service Id to add
     */
    @ApiMethod(name = "register")
    public void registerService(@Named("serviceId") String serviceId, @Named("serviceGroup") String serviceGroup) {
        if(findRecord(serviceId) != null) {
            log.info("Service " + serviceId + " already registered, skipping register");
            return;
        }
        ServiceRecord record = new ServiceRecord();
        record.setServiceId(serviceId);
        record.setServiceGroup(serviceGroup);
        ofy().save().entity(record).now();
    }

    /**
     * Update a service in the backend
     *
     * @param serviceId The Service Id to add
     * @param status The new service status
     */
    @ApiMethod(name = "update")
    public void updateService(@Named("serviceId") String serviceId, @Named("status") int status) {
        if(findRecord(serviceId) == null) {
            log.info("Service " + serviceId + " not registered, skipping update");
            return;
        }

        ServiceRecord record = findRecord(serviceId);
        record.setStatus(status);
        ofy().save().entity(record).now();
    }

    /**
     * Unregister a service from the backend
     *
     * @param serviceId The service Id to remove
     */
    @ApiMethod(name = "unregister")
    public void unregisterService(@Named("serviceId") String serviceId) {
        ServiceRecord record = findRecord(serviceId);
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

    private ServiceRecord findRecord(String serviceId) {
        return ofy().load().type(ServiceRecord.class).filter("serviceId", serviceId).first().now();
    }

}