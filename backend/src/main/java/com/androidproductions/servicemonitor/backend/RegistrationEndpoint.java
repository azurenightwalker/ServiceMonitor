package com.androidproductions.servicemonitor.backend;

import com.androidproductions.servicemonitor.backend.data.Registrations;
import com.androidproductions.servicemonitor.backend.data.models.RegistrationRecord;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.appengine.api.oauth.OAuthRequestException;
import com.google.appengine.api.users.User;

import javax.inject.Named;

/**
 * A registration endpoint class we are exposing for a device's GCM registration id on the backend
 *
 * For more information, see
 * https://developers.google.com/appengine/docs/java/endpoints/
 */
@Api(name = "registration",
        version = "v1.3",
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
public class RegistrationEndpoint {

    /**
     * Register a device to the backend
     *
     * @param record The Google Cloud Messaging registration to add
     */
    @ApiMethod(name = "register", httpMethod = "POST", path = "registrations")
    public RegistrationRecord registerDevice(RegistrationRecord record, User user) throws OAuthRequestException {
        if (user == null)
            throw new OAuthRequestException("User unauthorized");

        record.setUser(user.getEmail());

        return Registrations.create(record);
    }

    /**
     * Unregister a device from the backend
     *
     * @param regId The Google Cloud Messaging registration Id to remove
     */
    @ApiMethod(name = "unregister", httpMethod = "DELETE", path = "registrations/{regId}")
    public void unregisterDevice(@Named("regId") String regId, User user) throws OAuthRequestException {
        if (user == null)
            throw new OAuthRequestException("User unauthorized");

        Registrations.delete(regId);
    }

    /**
     * Return a collection of registered devices
     *
     * @return a list of Google Cloud Messaging registration Ids
     */
    @ApiMethod(name = "listDevices", httpMethod = "GET", path = "registrations")
    public CollectionResponse<RegistrationRecord> listDevices(User user) throws OAuthRequestException {
        if (user == null)
            throw new OAuthRequestException("User unauthorized");

        return CollectionResponse.<RegistrationRecord>builder().setItems(Registrations.search()).build();
    }
}