package com.androidproductions.servicemonitor.backend;

import com.androidproductions.servicemonitor.backend.data.Subscriptions;
import com.androidproductions.servicemonitor.backend.data.models.SubscriptionRecord;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.appengine.api.oauth.OAuthRequestException;
import com.google.appengine.api.users.User;

import java.util.logging.Logger;

import javax.inject.Named;

/**
 * An endpoint to send messages to devices registered with the backend
 *
 * For more information, see
 * https://developers.google.com/appengine/docs/java/endpoints/
 */
@Api(
        name = "subscriptions",
        version = "v1",
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
public class SubscriptionEndpoint {
    private static final Logger log = Logger.getLogger(SubscriptionEndpoint.class.getName());

    /**
     * Return a collection of all subscriptions
     *
     * @return a collection of all subscriptions
     */
    @ApiMethod(name = "listSubscriptions", httpMethod = "GET", path = "subscriptions")
    public CollectionResponse<SubscriptionRecord> listAllSubscriptions(User user) throws OAuthRequestException {
        if (user == null)
            throw new OAuthRequestException("User unauthorized");

        return CollectionResponse.<SubscriptionRecord>builder().setItems(Subscriptions.search(null)).build();
    }

    /**
     * Return a collection of a users subscriptions
     *
     * @param userId user id to get subscriptions for
     * @return a collection of a users subscriptions
     */
    @ApiMethod(name = "listUserSubscriptions", httpMethod = "GET", path = "subscriptions/{userId}")
    public CollectionResponse<SubscriptionRecord> listUserSubscriptions(@Named("userId") String userId, User user) throws OAuthRequestException {
        if (user == null)
            throw new OAuthRequestException("User unauthorized");

        return CollectionResponse.<SubscriptionRecord>builder().setItems(Subscriptions.search(userId)).build();
    }

    /**
     * Creates a new subscription
     *
     * @param userId user id to create subscription for
     * @return the new subscription
     */
    @ApiMethod(name = "createUserSubscription", httpMethod = "POST", path = "subscriptions/{userId}")
    public SubscriptionRecord createUserSubscription(@Named("userId") String userId, SubscriptionRecord record, User user) throws OAuthRequestException {
        if (user == null)
            throw new OAuthRequestException("User unauthorized");

        record.setUser(userId);

        return Subscriptions.create(record);
    }

    /**
     * Deletes a subscription
     *
     * @param userId user id to delete subscription for
     * @param subscriptionId subscriptionId to remove
     */
    @ApiMethod(name = "removeUserSubscription", httpMethod = "DELETE", path = "subscriptions/{userId}/{subscriptionId}")
    public void removeUserSubscription(@Named("userId") String userId, @Named("subscriptionId") String subscriptionId, User user) throws OAuthRequestException {
        if (user == null)
            throw new OAuthRequestException("User unauthorized");

        Subscriptions.delete(userId, subscriptionId);
    }


}
