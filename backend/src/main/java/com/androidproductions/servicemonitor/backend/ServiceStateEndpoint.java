package com.androidproductions.servicemonitor.backend;

import com.google.android.gcm.server.Message;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.appengine.api.oauth.OAuthRequestException;
import com.google.appengine.api.users.User;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import javax.inject.Named;

import static com.androidproductions.servicemonitor.backend.OfyService.ofy;

@Api(name = "services",
        version = "v1.4",
        namespace = @ApiNamespace(
                ownerDomain = "backend.servicemonitor.androidproductions.com",
                ownerName = "backend.servicemonitor.androidproductions.com",
                packagePath=""),
        clientIds = {
                Constants.WEB_CLIENT_ID,
                Constants.ANDROID_CLIENT_ID,
                Constants.IOS_CLIENT_ID,
                com.google.api.server.spi.Constant.API_EXPLORER_CLIENT_ID},
        audiences = {Constants.ANDROID_AUDIENCE},
        scopes = {Constants.EMAIL_SCOPE}
)
public class ServiceStateEndpoint {

    private static final Logger log = Logger.getLogger(ServiceStateEndpoint.class.getName());

    private static final int AlertLevel = 1<<2;

    /**
     * Register a service to the backend
     *
     * @param rec The Service to add
     */
    @ApiMethod(name = "register", path = "services/{group}", httpMethod = "POST")
    public void registerService(@Named("group") String group, ServiceRecord rec, User user) throws OAuthRequestException {
        if (user == null)
            throw new OAuthRequestException("User unauthorized");
        if(findRecord(rec.getServiceId(), group) != null) {
            log.info("Service " + rec.getServiceId() + " already registered, skipping register");
            return;
        }
        ofy().save().entity(rec).now();
    }

    /**
     * Update a service in the backend
     *
     * @param srvId The Service name to add
     * @param rec The new service record
     */
    @ApiMethod(name = "update", path = "services/{srvGroup}/{srvId}", httpMethod = "PUT")
    public void updateService(@Named("srvId") String srvId, @Named("srvGroup") String srvGroup, ServiceRecord rec, User user) throws OAuthRequestException {
        if (user == null)
            throw new OAuthRequestException("User unauthorized");

        if(findRecord(srvId,srvGroup) == null) {
            log.info("Service " + srvId + " not registered, skipping update");
            return;
        }

        ServiceRecord record = findRecord(srvId, srvGroup);
        record.setStatus(rec.getStatus());
        if (rec.getClaimant() != null)
            record.setClaimant(rec.getClaimant());
        ofy().save().entity(record).now();

        if (record.getStatus() >= AlertLevel && !record.isAlerted())
        {
            SendAlert(record);
            record.setAlerted(true);
        }
        else if (record.isAlerted() && record.getStatus() < AlertLevel)
        {
            SendAlert(record);
            record.setClaimant(null);
            record.setAlerted(false);
        }
        ofy().save().entity(record).now();
    }

    private void SendAlert(ServiceRecord record) {
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

    /**
     * Unregister a service from the backend
     *
     * @param serviceId The service Id to remove
     */
    @ApiMethod(name = "unregister", httpMethod = "DELETE", path = "services/{serviceGroup}/{serviceId}")
    public void unregisterService(@Named("serviceGroup") String serviceGroup, @Named("serviceId") String serviceId, User user) throws OAuthRequestException {
        if (user == null)
            throw new OAuthRequestException("User unauthorized");

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
    public CollectionResponse<ServiceRecord> listServices(@Named("serviceGroup") String serviceGroup, User user) throws OAuthRequestException {
        if (user == null)
            throw new OAuthRequestException("User unauthorized");

        List<ServiceRecord> records = ofy().transactionless().load().type(ServiceRecord.class)
                .filter("serviceGroup", serviceGroup).list();
        return CollectionResponse.<ServiceRecord>builder().setItems(records).build();
    }

    /**
     * Return a collection of registered services
     *
     * @param serviceGroup The group of services to list
     * @return a list of services
     */
    @ApiMethod(name = "get", httpMethod = "GET", path = "services/{serviceGroup}/{serviceId}")
    public ServiceRecord getService(@Named("serviceGroup") String serviceGroup,@Named("serviceId") String serviceId, User user) throws OAuthRequestException {
        if (user == null)
            throw new OAuthRequestException("User unauthorized");

        return findRecord(serviceId,serviceGroup);
    }

    private ServiceRecord findRecord(String serviceId, String serviceGroup) {
        return ofy().load().type(ServiceRecord.class)
                .filter("serviceId", serviceId)
                .filter("serviceGroup", serviceGroup)
                .first().now();
    }

}