package cmpe277.project.skibuddy.server;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import cmpe277.project.skibuddy.common.NotAuthenticatedException;
import cmpe277.project.skibuddy.common.SkiBuddyLocation;
import cmpe277.project.skibuddy.common.SkiBuddyLocationListener;
import cmpe277.project.skibuddy.common.User;

/**
 * Created by eh on 12/11/2015.
 */
public class UpdateClient extends SkiBuddyLocationListener implements LiveServer, WebsocketStatusListener {
    private List<SkiBuddyLocationListener> listeners = new LinkedList<>();
    private LiveClient liveClient;
    private AuthenticatedUserProvider authenticatedUserProvider;
    private Handler handler;
    private URI serverURI;
    private boolean shouldConnect = false;
    private Context context;
    private UUID eventID = null;
    private SkiBuddyLocation lastLocation = null;

    public UpdateClient(Context context, AuthenticatedUserProvider authenticatedUserProvider){
        this.authenticatedUserProvider = authenticatedUserProvider;
        this.context = context;

        try {
            serverURI = new URI("ws://ec2-52-53-211-23.us-west-1.compute.amazonaws.com:8282");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        handler = new Handler();
    }

    /**
     * Return first and last initial
     * If input is "John F. Kennedy", we just want "JK"
     * If the user only has one name, only return a single initial
     * @param user
     * @return
     */
    private String getInitials(User user){
        String name = user.getName();
        if(name.length() < 1) return "";
        String initials = name.substring(0,1).toUpperCase();
        final int lastSpaceIndex = name.lastIndexOf(" ");
        if(lastSpaceIndex > -1 && name.length() > lastSpaceIndex + 1){
            initials += name.substring(lastSpaceIndex + 1, lastSpaceIndex + 2).toUpperCase();
        }
        return initials;
    }

    @Override
    public void updateLocation(SkiBuddyLocation location, UUID eventID) {
        lastLocation = location;
        eventID = eventID;

        // Send update if we're connected, discard it if we aren't
        try {
            User authenticatedUser = authenticatedUserProvider.getAuthenticatedUser();
            if (liveClient != null)
                liveClient.sendUpdate(authenticatedUser.getId(), eventID, location, getInitials(authenticatedUser));
        } catch (NotAuthenticatedException e){
            // Ignore updates for not authenticated users
        }
    }

    @Override
    public void registerLocationListener(SkiBuddyLocationListener listener) {
        listeners.add(listener);

        if(!shouldConnect){
            // We were disconnected, so we need to reconnect
            Log.d(UpdateClient.class.getName(), "Listener attached, connecting");
            shouldConnect = true;
            handler.post(connect);
        }
    }

    @Override
    public void unregisterLocationListener(SkiBuddyLocationListener listener) {
        listeners.remove(listener);

        if(listeners.size() == 0){
            shouldConnect = false;
            liveClient.close();
        }
    }

    /**
     * Forward observations to all registered listeners
     */
    @Override
    public void getLocationUpdate(UUID user, String initials, SkiBuddyLocation location) {
        Log.d(UpdateClient.class.getName(),
                "Received a new location for user with initials " + initials);

        if(listeners.size() > 0){
            for (SkiBuddyLocationListener listener : listeners){
                if(listener != null) {
                    listener.postUpdate(user, initials, location);
                    Handler mainHandler = new Handler(context.getMainLooper());
                    mainHandler.post(listener);
                }
            }
        }
    }

    @Override
    public void onConnectionOpened() {
        Log.i(UpdateClient.class.getName(), "Connected to websocket server");

        // If we had a previous update to send, resend that
        if(eventID != null && lastLocation != null)
            updateLocation(lastLocation, eventID);
    }

    @Override
    public void onConnectionClosed(int code, String reason, boolean remote) {
        // If the connection closed, let's try to reconnect after 2 seconds
        liveClient = null;

        Log.i(UpdateClient.class.getName(), "Lost connection to websocket server: " + reason);

        int postAfter = 2000; // milliseconds
        handler.postDelayed(connect, postAfter);
    }

    final private UpdateClient self = this;
    private Runnable connect = new Runnable() {
        @Override
        public void run() {
            if(shouldConnect && liveClient == null) {
                Log.d(UpdateClient.class.getName(), "Connecting...");
                liveClient = new LiveClient(serverURI, self, self);
            }
        }
    };
}
