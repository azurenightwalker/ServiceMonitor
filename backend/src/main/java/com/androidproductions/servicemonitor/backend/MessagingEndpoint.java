package com.androidproductions.servicemonitor.backend;

import com.google.android.gcm.server.Message;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.appengine.api.oauth.OAuthRequestException;
import com.google.appengine.api.users.User;

import java.io.IOException;
import java.util.logging.Logger;
import javax.inject.Named;

/**
 * An endpoint to send messages to devices registered with the backend
 *
 * For more information, see
 * https://developers.google.com/appengine/docs/java/endpoints/
 */
@Api(
        name = "messaging",
        version = "v1.1",
        namespace = @ApiNamespace(
                ownerDomain = "backend.servicemonitor.androidproductions.com",
                ownerName = "backend.servicemonitor.androidproductions.com",
                packagePath=""),
        clientIds = {
                com.androidproductions.servicemonitor.backend.Constants.WEB_CLIENT_ID,
                com.androidproductions.servicemonitor.backend.Constants.ANDROID_CLIENT_ID,
                com.androidproductions.servicemonitor.backend.Constants.IOS_CLIENT_ID,
                com.google.api.server.spi.Constant.API_EXPLORER_CLIENT_ID},
        audiences = {com.androidproductions.servicemonitor.backend.Constants.ANDROID_AUDIENCE},
        scopes = {com.androidproductions.servicemonitor.backend.Constants.EMAIL_SCOPE}
)
public class MessagingEndpoint {
    private static final Logger log = Logger.getLogger(MessagingEndpoint.class.getName());

    /**
     * Send to the first 10 devices (You can modify this to send to any number of devices or a specific device)
     *
     * @param message The message to send
     */
    public void sendMessage(@Named("message") String message, User user) throws IOException, OAuthRequestException {
        if (user == null)
            throw new OAuthRequestException("User unauthorized");
        if(message == null || message.trim().length() == 0) {
            log.warning("Not sending message because it is empty");
            return;
        }
        // crop longer messages
        if (message.length() > 1000) {
            message = message.substring(0, 1000) + "[...]";
        }
        Message msg = new Message.Builder().addData("message", message).build();
        GcmHelper.SendMessage(msg);
    }


}
