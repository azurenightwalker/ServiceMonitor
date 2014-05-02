package com.androidproductions.servicemonitor.backend;

import com.androidproductions.servicemonitor.backend.data.ServiceGroups;
import com.androidproductions.servicemonitor.backend.data.Services;
import com.androidproductions.servicemonitor.backend.data.models.ServiceGroupRecord;
import com.androidproductions.servicemonitor.backend.data.models.ServiceRecord;
import com.google.android.gcm.server.Message;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.appengine.api.oauth.OAuthRequestException;
import com.google.appengine.api.users.User;

import java.io.IOException;
import java.util.logging.Logger;

import javax.inject.Named;

@Api(name = "services",
        version = "v1.7",
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

    // Service Groups

    /**
     * Register a service group to the backend
     *
     * @param rec The Service group to add
     */
    @ApiMethod(name = "registerServiceGroup", path = "services", httpMethod = "POST")
    public ServiceGroupRecord registerServiceGroup(ServiceGroupRecord rec, User user) throws OAuthRequestException {
        if (user == null)
            throw new OAuthRequestException("User unauthorized");

        return ServiceGroups.create(rec);
    }

    /**
     * Return a collection of registered service groups
     *
     * @return a list of service groups
     */
    @ApiMethod(name = "listServiceGroups", httpMethod = "GET", path = "services")
    public CollectionResponse<ServiceGroupRecord> listServiceGroups(User user) throws OAuthRequestException {
        if (user == null)
            throw new OAuthRequestException("User unauthorized");

        return CollectionResponse.<ServiceGroupRecord>builder()
                .setItems(ServiceGroups.search()).build();
    }

    /**
     * Update a service group in the backend
     *
     * @param group The Service group to update
     * @param rec The updated group
     */
    @ApiMethod(name = "updateServiceGroup", path = "services/{group}", httpMethod = "PUT")
    public void updateServiceGroup(@Named("group") String group, ServiceGroupRecord rec, User user) throws OAuthRequestException {
        if (user == null)
            throw new OAuthRequestException("User unauthorized");

        // TODO: Implement
    }

    /**
     * Delete a service group from the backend
     *
     * @param group The Service group to delete
     */
    @ApiMethod(name = "deleteServiceGroup", path = "services/{group}", httpMethod = "DELETE")
    public void deleteServiceGroup(@Named("group") String group, User user) throws OAuthRequestException {
        if (user == null)
            throw new OAuthRequestException("User unauthorized");

        // TODO: Implement
    }

    // Services

    /**
     * Register a service to the backend
     *
     * @param rec The Service to add
     */
    @ApiMethod(name = "registerService", path = "services/{group}", httpMethod = "POST")
    public ServiceRecord registerService(@Named("group") String group, ServiceRecord rec, User user) throws OAuthRequestException {
        if (user == null)
            throw new OAuthRequestException("User unauthorized");

        rec.setServiceGroup(group);

        return Services.create(rec);
    }

    /**
     * Return a collection of registered services
     *
     * @param serviceGroup The group of services to list
     * @return a list of services
     */
    @ApiMethod(name = "listServices", httpMethod = "GET", path = "services/{serviceGroup}")
    public CollectionResponse<ServiceRecord> listServices(@Named("serviceGroup") String serviceGroup, User user) throws OAuthRequestException {
        if (user == null)
            throw new OAuthRequestException("User unauthorized");

        return CollectionResponse.<ServiceRecord>builder()
                .setItems(Services.search(serviceGroup)).build();
    }

    /**
     * Return a single service
     *
     * @param serviceGroup The group of services to look in
     * @param serviceId The name of the service
     * @return a service record
     */
    @ApiMethod(name = "get", httpMethod = "GET", path = "services/{serviceGroup}/{serviceId}")
    public ServiceRecord getService(@Named("serviceGroup") String serviceGroup,@Named("serviceId") String serviceId, User user) throws OAuthRequestException {
        if (user == null)
            throw new OAuthRequestException("User unauthorized");

        return Services.find(serviceId, serviceGroup);
    }

    /**
     * Update a service in the backend
     *
     * @param srvId The Service name to add
     * @param rec The new service record
     */
    @ApiMethod(name = "update", path = "services/{srvGroup}/{srvId}", httpMethod = "PUT")
    public ServiceRecord updateService(@Named("srvId") String srvId, @Named("srvGroup") String srvGroup, ServiceRecord rec, User user) throws OAuthRequestException {
        if (user == null)
            throw new OAuthRequestException("User unauthorized");

        ServiceRecord record = Services.update(srvId,srvGroup,rec);

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

        Services.update(srvId, srvGroup, record);

        return record;
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

        Services.delete(serviceId, serviceGroup);
    }


    // Misc
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
}