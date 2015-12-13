package cmpe277.project.skibuddy.server;

import java.util.UUID;

import cmpe277.project.skibuddy.common.SkiBuddyLocation;
import cmpe277.project.skibuddy.common.SkiBuddyLocationListener;

/**
 * Created by eh on 12/11/2015.
 */
public interface LiveServer {
    /**
     * Sends a location update for the current user. Used to share the user's position with others
     * in the same event
     */
    void updateLocation(SkiBuddyLocation location, UUID eventID);

    /**
     * Registers a location listener. The server will automatically send location updates for all
     * users in the event in which we're sending our own updates.
     */
    void registerLocationListener(SkiBuddyLocationListener listener);

    /**
     * Unregisters the specified location listener
     */
    void unregisterLocationListener(SkiBuddyLocationListener listener);
}
